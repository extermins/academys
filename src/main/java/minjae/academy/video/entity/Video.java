package minjae.academy.video.entity;

import jakarta.persistence.*;
import lombok.*;
import minjae.academy.config.JpaAuditing.BaseEntity;
import minjae.academy.enums.VideoStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String storedFilename;

    @Column
    private String thumbnailFilename;

    @Column
    private String thumbnailType;  // AUTO: 자동생성, MANUAL: 수동업로드

    @Column
    private String convertedFilename;

    @Column(nullable = false)
    private String filePath;

    @Column
    private Long fileSize;

    @Column
    private Double duration;

    @Column
    private Integer width;

    @Column
    private Integer height;

    @Column
    private String codec;

    @Column
    private Long bitrate;

    @Column
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private VideoStatus status = VideoStatus.UPLOADED;

    @Column
    private String errorMessage;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum VideoStatus {
        UPLOADED,           // 업로드 완료
        PROCESSING,         // 처리 중
        THUMBNAIL_CREATED,  // 썸네일 생성 완료
        CONVERTED,          // 변환 완료
        COMPLETED,          // 모든 처리 완료
        ERROR               // 오류 발생
    }

    // 포맷된 재생시간 반환
    public String getFormattedDuration() {
        if (duration == null) return "00:00:00";
        int hours = (int) (duration / 3600);
        int minutes = (int) ((duration % 3600) / 60);
        int seconds = (int) (duration % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // 해상도 문자열 반환
    public String getResolution() {
        if (width == null || height == null) return "Unknown";
        return width + "x" + height;
    }

    // 파일 크기 포맷
    public String getFormattedFileSize() {
        if (fileSize == null) return "Unknown";
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.2f KB", fileSize / 1024.0);
        if (fileSize < 1024 * 1024 * 1024) return String.format("%.2f MB", fileSize / (1024.0 * 1024));
        return String.format("%.2f GB", fileSize / (1024.0 * 1024 * 1024));
    }
}
