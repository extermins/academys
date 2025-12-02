package minjae.academy.config.Ffmpeg;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FfmpegConfig {
    @Value("${ffmpeg.path:/usr/bin/ffmpeg}")
    private String ffmpegPath;

    @Value("${ffprobe.path:/usr/bin/ffprobe}")
    private String ffprobePath;

    @Bean
    public FFmpeg ffmpeg() throws IOException {
        return new FFmpeg(ffmpegPath);
    }

    @Bean
    public FFprobe ffprobe() throws IOException {
        return new FFprobe(ffprobePath);
    }

    @Bean
    public FFmpegExecutor ffmpegExecutor(FFmpeg ffmpeg, FFprobe ffprobe) {
        return new FFmpegExecutor(ffmpeg, ffprobe);
    }
}
