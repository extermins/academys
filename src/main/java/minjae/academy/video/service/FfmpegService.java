package minjae.academy.video.service;

import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjae.academy.video.dto.VideoMetaDataDto;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FfmpegService {
    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;
    private final FFmpegExecutor executor;

    /**
     * 비디오 정보 조회
     */
    public FFmpegProbeResult getVideoInfo(String filePath) throws IOException {
        return ffprobe.probe(filePath);
    }

    /**
     * 비디오 메타데이터 추출
     */
    public VideoMetaDataDto extractMetadata(String filePath) throws IOException {
        FFmpegProbeResult probeResult = ffprobe.probe(filePath);

        FFmpegStream videoStream = probeResult.getStreams().stream()
                .filter(s -> s.codec_type == FFmpegStream.CodecType.VIDEO)
                .findFirst()
                .orElse(null);

        if (videoStream == null) {
            throw new IllegalArgumentException("비디오 스트림을 찾을 수 없습니다: " + filePath);
        }

        return VideoMetaDataDto.builder()
                .duration(probeResult.getFormat().duration)
                .width(videoStream.width)
                .height(videoStream.height)
                .codec(videoStream.codec_name)
                .bitrate(probeResult.getFormat().bit_rate)
                .format(probeResult.getFormat().format_name)
                .build();
    }

    /**
     * 썸네일 생성
     */
    public void generateThumbnail(String inputPath, String outputPath, int seconds) throws IOException {
        log.info("썸네일 생성 시작: {} -> {} ({}초 지점)", inputPath, outputPath, seconds);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .overrideOutputFiles(true)
                .addOutput(outputPath)
                .setFrames(1)
                .setVideoFilter("select='gte(t," + seconds + ")',scale=320:-1")
                .done();

        executor.createJob(builder).run();
        log.info("썸네일 생성 완료: {}", outputPath);
    }

    /**
     * 비디오 변환 (MP4로)
     */
    public void convertToMp4(String inputPath, String outputPath) throws IOException {
        log.info("MP4 변환 시작: {} -> {}", inputPath, outputPath);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .overrideOutputFiles(true)
                .addOutput(outputPath)
                .setFormat("mp4")
                .setVideoCodec("libx265")
                .setAudioCodec("aac")
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

        executor.createJob(builder, progress -> {
            if (progress.out_time_ns > 0) {
                log.debug("변환 진행: {} ns", progress.out_time_ns);
            }
        }).run();

        log.info("MP4 변환 완료: {}", outputPath);
    }

    /**
     * 비디오 해상도 변경
     */
    public void resizeVideo(String inputPath, String outputPath, int width, int height) throws IOException {
        log.info("비디오 리사이즈 시작: {}x{}", width, height);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .overrideOutputFiles(true)
                .addOutput(outputPath)
                .setVideoResolution(width, height)
                .setVideoCodec("libx265")
                //libx264 에서 수정 함
                .setAudioCodec("copy")
                .done();

        executor.createJob(builder).run();
        log.info("비디오 리사이즈 완료: {}x{}", width, height);
    }

    /**
     * 오디오 추출
     */
    public void extractAudio(String inputPath, String outputPath) throws IOException {
        log.info("오디오 추출 시작: {}", inputPath);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .overrideOutputFiles(true)
                .addOutput(outputPath)
                .disableVideo()
                .setAudioCodec("libmp3lame")
                .setAudioBitRate(192000)
                .done();

        executor.createJob(builder).run();
        log.info("오디오 추출 완료: {}", outputPath);
    }

    /**
     * HLS 스트리밍용 변환
     */
    public void convertToHls(String inputPath, String outputDir, String playlistName) throws IOException {
        log.info("HLS 변환 시작: {}", inputPath);

        String outputPath = outputDir + "/" + playlistName + ".m3u8";
        String segmentPath = outputDir + "/" + playlistName + "_%03d.ts";

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .overrideOutputFiles(true)
                .addOutput(outputPath)
                .setFormat("hls")
                .setVideoCodec("libx265")
                .setAudioCodec("aac")
                .addExtraArgs("-hls_time", "10")
                .addExtraArgs("-hls_list_size", "0")
                .addExtraArgs("-hls_segment_filename", segmentPath)
                .done();

        executor.createJob(builder).run();
        log.info("HLS 변환 완료: {}", outputPath);
    }

    /**
     * 비디오 자르기
     */
    public void cutVideo(String inputPath, String outputPath, double startSeconds, double endSeconds) throws IOException {
        log.info("비디오 자르기 시작: {} ~ {} 초", startSeconds, endSeconds);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .overrideOutputFiles(true)
                .addOutput(outputPath)
                .setStartOffset((long) startSeconds, java.util.concurrent.TimeUnit.SECONDS)
                .setDuration((long) (endSeconds - startSeconds), java.util.concurrent.TimeUnit.SECONDS)
                .setVideoCodec("copy")
                .setAudioCodec("copy")
                .done();

        executor.createJob(builder).run();
        log.info("비디오 자르기 완료: {}", outputPath);
    }

}
