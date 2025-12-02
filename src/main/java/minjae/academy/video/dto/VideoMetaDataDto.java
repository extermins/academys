package minjae.academy.video.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoMetaDataDto {
    private double duration;
    private int width;
    private int height;
    private String codec;
    private long bitrate;
    private String format;

    public String getFormattedDuration() {
        int hours = (int) (duration / 3600);
        int minutes = (int) ((duration % 3600) / 60);
        int seconds = (int) (duration % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public String getResolution() {
        return width + "x" + height;
    }

    public String getFormattedBitrate() {
        if (bitrate < 1000) return bitrate + " bps";
        if (bitrate < 1000000) return String.format("%.2f Kbps", bitrate / 1000.0);
        return String.format("%.2f Mbps", bitrate / 1000000.0);
    }
}
