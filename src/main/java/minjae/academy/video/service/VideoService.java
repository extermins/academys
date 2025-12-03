package minjae.academy.video.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjae.academy.video.dto.VideoMetaDataDto;
import minjae.academy.video.entity.Video;
import minjae.academy.video.repository.VideoRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VideoService {
    private final VideoRepository videoRepository;
    private final FfmpegService ffmpegService;

    @Value("${upload.video-path}")
    private String videoPath;

    @Value("${upload.thumbnail-path}")
    private String thumbnailPath;

    @Value("${upload.converted-path}")
    private String convertedPath;

    @Value("${thumbnail.auto-generate:true}")
    private boolean thumbnailAutoGenerate;

    @Value("${thumbnail.default-capture-time:5}")
    private int defaultCaptureTime;

    public Video uploadVideo(MultipartFile file) throws IOException {
        // 디렉토리 생성
        createDirectories();

        // 파일명 생성
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String storedFilename = UUID.randomUUID().toString() + "." + extension;

        // 파일 저장
        Path filePath = Paths.get(videoPath, storedFilename);
        Files.copy(file.getInputStream(), filePath);

        // 엔티티 생성 및 저장
        Video video = Video.builder()
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .filePath(filePath.toString())
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .status(Video.VideoStatus.UPLOADED)
                .build();

        video = videoRepository.save(video);
        log.info("비디오 업로드 완료: {}", storedFilename);

        return video;
    }

    /**
     * 비디오 메타데이터 추출 및 업데이트
     */
    public Video extractAndSaveMetadata(String videoId) throws IOException {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("비디오를 찾을 수 없습니다: " + videoId));

        try {
            VideoMetaDataDto metadata = ffmpegService.extractMetadata(video.getFilePath());

            video.setDuration(metadata.getDuration());
            video.setWidth(metadata.getWidth());
            video.setHeight(metadata.getHeight());
            video.setCodec(metadata.getCodec());
            video.setBitrate(metadata.getBitrate());
            video.setStatus(Video.VideoStatus.PROCESSING);

            return videoRepository.save(video);
        } catch (Exception e) {
            log.error("메타데이터 추출 실패: {}", e.getMessage());
            video.setStatus(Video.VideoStatus.ERROR);
            video.setErrorMessage("메타데이터 추출 실패: " + e.getMessage());
            return videoRepository.save(video);
        }
    }

    /**
     * 썸네일 생성 (자동 - 기본 시간)
     */
    public Video generateThumbnail(String videoId) throws IOException {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("비디오를 찾을 수 없습니다: " + videoId));

        try {
            String thumbnailFilename = FilenameUtils.getBaseName(video.getStoredFilename()) + ".jpg";
            String thumbnailFullPath = Paths.get(thumbnailPath, thumbnailFilename).toString();

            // 기본 캡처 시간 또는 영상 길이의 10% 지점에서 썸네일 생성
            int captureTime = video.getDuration() != null ?
                    Math.min(defaultCaptureTime, (int)(video.getDuration() * 0.1)) : 1;

            ffmpegService.generateThumbnail(video.getFilePath(), thumbnailFullPath, captureTime);

            video.setThumbnailFilename(thumbnailFilename);
            video.setThumbnailType("AUTO");
            video.setStatus(Video.VideoStatus.THUMBNAIL_CREATED);

            return videoRepository.save(video);
        } catch (Exception e) {
            log.error("썸네일 생성 실패: {}", e.getMessage());
            video.setStatus(Video.VideoStatus.ERROR);
            video.setErrorMessage("썸네일 생성 실패: " + e.getMessage());
            return videoRepository.save(video);
        }
    }

    /**
     * 썸네일 수동 업로드 (사용자가 직접 이미지 업로드)
     */
    public Video uploadThumbnail(String videoId, MultipartFile thumbnailFile) throws IOException {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("비디오를 찾을 수 없습니다: " + videoId));

        // 이미지 파일 검증
        String contentType = thumbnailFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다. (JPG, PNG, GIF, WEBP)");
        }

        try {
            // 기존 썸네일 삭제
            if (video.getThumbnailFilename() != null) {
                Files.deleteIfExists(Paths.get(thumbnailPath, video.getThumbnailFilename()));
            }

            // 새 썸네일 저장
            String extension = FilenameUtils.getExtension(thumbnailFile.getOriginalFilename());
            if (extension == null || extension.isEmpty()) {
                extension = "jpg";
            }
            String thumbnailFilename = FilenameUtils.getBaseName(video.getStoredFilename()) + "_thumb." + extension;
            Path thumbnailFullPath = Paths.get(thumbnailPath, thumbnailFilename);

            Files.copy(thumbnailFile.getInputStream(), thumbnailFullPath,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            video.setThumbnailFilename(thumbnailFilename);
            video.setThumbnailType("MANUAL");

            // 상태 업데이트
            if (video.getStatus() == Video.VideoStatus.ERROR ||
                    video.getStatus() == Video.VideoStatus.PROCESSING) {
                video.setStatus(Video.VideoStatus.THUMBNAIL_CREATED);
                video.setErrorMessage(null);
            }

            log.info("수동 썸네일 업로드 완료: videoId={}, filename={}", videoId, thumbnailFilename);
            return videoRepository.save(video);
        } catch (Exception e) {
            log.error("수동 썸네일 업로드 실패: {}", e.getMessage());
            throw new IOException("썸네일 업로드 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 썸네일 삭제
     */
    public Video deleteThumbnail(String videoId) throws IOException {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("비디오를 찾을 수 없습니다: " + videoId));

        if (video.getThumbnailFilename() != null) {
            Files.deleteIfExists(Paths.get(thumbnailPath, video.getThumbnailFilename()));
            video.setThumbnailFilename(null);
            video.setThumbnailType(null);
            log.info("썸네일 삭제 완료: videoId={}", videoId);
        }

        return videoRepository.save(video);
    }

    /**
     * 썸네일 자동 생성 설정 여부 조회
     */
    public boolean isThumbnailAutoGenerate() {
        return thumbnailAutoGenerate;
    }

    /**
     * MP4로 변환
     */
    public Video convertToMp4(String videoId) throws IOException {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("비디오를 찾을 수 없습니다: " + videoId));

        try {
            String convertedFilename = FilenameUtils.getBaseName(video.getStoredFilename()) + "_converted.mp4";
            String convertedFullPath = Paths.get(convertedPath, convertedFilename).toString();

            video.setStatus(Video.VideoStatus.PROCESSING);
            videoRepository.save(video);

            ffmpegService.convertToMp4(video.getFilePath(), convertedFullPath);

            video.setConvertedFilename(convertedFilename);
            video.setStatus(Video.VideoStatus.CONVERTED);

            return videoRepository.save(video);
        } catch (Exception e) {
            log.error("비디오 변환 실패: {}", e.getMessage());
            video.setStatus(Video.VideoStatus.ERROR);
            video.setErrorMessage("비디오 변환 실패: " + e.getMessage());
            return videoRepository.save(video);
        }
    }

    /**
     * 비디오 전체 처리 (업로드 후 메타데이터 추출, 썸네일 생성)
     * autoThumbnail 파라미터로 썸네일 자동 생성 여부 제어
     */
    public Video processVideo(MultipartFile file, boolean autoThumbnail) throws IOException {
        // 1. 업로드
        Video video = uploadVideo(file);

        // 2. 메타데이터 추출
        video = extractAndSaveMetadata(video.getId());

        // 3. 썸네일 생성 (자동 생성이 활성화된 경우에만)
        if (video.getStatus() != Video.VideoStatus.ERROR && autoThumbnail) {
            video = generateThumbnail(video.getId());
        }

        // 4. 상태 업데이트
        if (video.getStatus() != Video.VideoStatus.ERROR) {
            if (autoThumbnail) {
                video.setStatus(Video.VideoStatus.COMPLETED);
            } else {
                video.setStatus(Video.VideoStatus.PROCESSING);  // 썸네일 대기 상태
            }
            video = videoRepository.save(video);
        }

        return video;
    }

    /**
     * 비디오 전체 처리 (기본 설정 사용)
     */
    public Video processVideo(MultipartFile file) throws IOException {
        return processVideo(file, thumbnailAutoGenerate);
    }

    /**
     * 비동기 비디오 변환
     */
    @Async
    public CompletableFuture<Video> convertVideoAsync(String videoId) {
        try {
            Video video = convertToMp4(videoId);
            return CompletableFuture.completedFuture(video);
        } catch (IOException e) {
            log.error("비동기 변환 실패", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * 전체 비디오 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Video> getAllVideos() {
        return videoRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 비디오 목록 조회 (페이지네이션)
     */
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<Video> getVideos(org.springframework.data.domain.Pageable pageable) {
        return videoRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    /**
     * 비디오 검색 (페이지네이션)
     */
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<Video> searchVideos(String keyword, Video.VideoStatus status,
                                                                     org.springframework.data.domain.Pageable pageable) {
        return videoRepository.searchVideos(keyword, status, pageable);
    }

    /**
     * 비디오 상세 조회
     */
    @Transactional(readOnly = true)
    public Video getVideo(String id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("비디오를 찾을 수 없습니다: " + id));
    }

    /**
     * 비디오 삭제
     */
    public void deleteVideo(String id) {
        Video video = getVideo(id);

        try {
            // 원본 파일 삭제
            Files.deleteIfExists(Paths.get(video.getFilePath()));

            // 썸네일 삭제
            if (video.getThumbnailFilename() != null) {
                Files.deleteIfExists(Paths.get(thumbnailPath, video.getThumbnailFilename()));
            }

            // 변환 파일 삭제
            if (video.getConvertedFilename() != null) {
                Files.deleteIfExists(Paths.get(convertedPath, video.getConvertedFilename()));
            }
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
        }

        videoRepository.delete(video);
        log.info("비디오 삭제 완료: {}", id);
    }

    /**
     * 업로드 디렉토리 생성
     */
    private void createDirectories() throws IOException {
        Files.createDirectories(Paths.get(videoPath));
        Files.createDirectories(Paths.get(thumbnailPath));
        Files.createDirectories(Paths.get(convertedPath));
    }

}
