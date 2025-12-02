package minjae.academy.video.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjae.academy.video.dto.VideoResponseDto;
import minjae.academy.video.entity.Video;
import minjae.academy.video.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/videos")
@Slf4j
public class VideoApiController {
    private final VideoService videoService;

    /**
     * 비디오 업로드 (전체 처리)
     * autoThumbnail 파라미터로 썸네일 자동 생성 여부 제어
     */
    @PostMapping("/upload")
    public ResponseEntity<VideoResponseDto<Video>> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "autoThumbnail", required = false, defaultValue = "true") boolean autoThumbnail) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(VideoResponseDto.error("파일을 선택해주세요."));
        }

        try {
            Video video = videoService.processVideo(file, autoThumbnail);
            String message = autoThumbnail ? "비디오 업로드 및 처리 완료" : "비디오 업로드 완료 (썸네일 수동 업로드 필요)";
            return ResponseEntity.ok(VideoResponseDto.success(message, video));
        } catch (IOException e) {
            log.error("비디오 업로드 실패", e);
            return ResponseEntity.internalServerError()
                    .body(VideoResponseDto.error("업로드 실패: " + e.getMessage()));
        }
    }

    /**
     * 전체 비디오 목록
     */
    @GetMapping
    public ResponseEntity<VideoResponseDto<List<Video>>> getAllVideos() {
        List<Video> videos = videoService.getAllVideos();
        return ResponseEntity.ok(VideoResponseDto.success(videos));
    }

    /**
     * 비디오 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<VideoResponseDto<Video>> getVideo(@PathVariable String id) {
        try {
            Video video = videoService.getVideo(id);
            return ResponseEntity.ok(VideoResponseDto.success(video));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 비디오 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<VideoResponseDto<Void>> deleteVideo(@PathVariable String id) {
        try {
            videoService.deleteVideo(id);
            return ResponseEntity.ok(VideoResponseDto.success("삭제 완료", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * MP4 변환 요청
     */
    @PostMapping("/{id}/convert")
    public ResponseEntity<VideoResponseDto<Video>> convertVideo(@PathVariable String id) {
        try {
            Video video = videoService.convertToMp4(id);
            return ResponseEntity.ok(VideoResponseDto.success("변환 완료", video));
        } catch (IOException e) {
            log.error("비디오 변환 실패", e);
            return ResponseEntity.internalServerError()
                    .body(VideoResponseDto.error("변환 실패: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 비동기 변환 요청
     */
    @PostMapping("/{id}/convert-async")
    public ResponseEntity<VideoResponseDto<Map<String, Object>>> convertVideoAsync(@PathVariable String id) {
        try {
            videoService.convertVideoAsync(id);
            return ResponseEntity.ok(VideoResponseDto.success("변환 작업이 시작되었습니다.",
                    Map.of("videoId", id, "status", "PROCESSING")));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(VideoResponseDto.error("변환 요청 실패: " + e.getMessage()));
        }
    }

    /**
     * 썸네일 자동 생성 (FFmpeg로 비디오에서 추출)
     */
    @PostMapping("/{id}/thumbnail/auto")
    public ResponseEntity<VideoResponseDto<Video>> generateThumbnailAuto(@PathVariable String id) {
        try {
            Video video = videoService.generateThumbnail(id);
            return ResponseEntity.ok(VideoResponseDto.success("썸네일 자동 생성 완료", video));
        } catch (IOException e) {
            log.error("썸네일 자동 생성 실패", e);
            return ResponseEntity.internalServerError()
                    .body(VideoResponseDto.error("썸네일 생성 실패: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 썸네일 수동 업로드 (사용자가 직접 이미지 업로드)
     */
    @PostMapping("/{id}/thumbnail/upload")
    public ResponseEntity<VideoResponseDto<Video>> uploadThumbnail(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(VideoResponseDto.error("썸네일 이미지를 선택해주세요."));
        }

        try {
            Video video = videoService.uploadThumbnail(id, file);
            return ResponseEntity.ok(VideoResponseDto.success("썸네일 업로드 완료", video));
        } catch (IOException e) {
            log.error("썸네일 업로드 실패", e);
            return ResponseEntity.internalServerError()
                    .body(VideoResponseDto.error("썸네일 업로드 실패: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(VideoResponseDto.error(e.getMessage()));
        }
    }

    /**
     * 썸네일 삭제
     */
    @DeleteMapping("/{id}/thumbnail")
    public ResponseEntity<VideoResponseDto<Video>> deleteThumbnail(@PathVariable String id) {
        try {
            Video video = videoService.deleteThumbnail(id);
            return ResponseEntity.ok(VideoResponseDto.success("썸네일 삭제 완료", video));
        } catch (IOException e) {
            log.error("썸네일 삭제 실패", e);
            return ResponseEntity.internalServerError()
                    .body(VideoResponseDto.error("썸네일 삭제 실패: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 썸네일 자동 생성 설정 조회
     */
    @GetMapping("/settings/thumbnail-auto")
    public ResponseEntity<VideoResponseDto<Map<String, Boolean>>> getThumbnailAutoSetting() {
        return ResponseEntity.ok(VideoResponseDto.success(
                Map.of("autoGenerate", videoService.isThumbnailAutoGenerate())));
    }
}
