package minjae.academy.video.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import minjae.academy.config.JpaAuditing.BaseEntity;
import minjae.academy.enums.VideoStatus;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    // === 기본 정보 ===
    @Column(nullable = false, length = 200)
    private String title;  // 동영상 제목

    @Column(columnDefinition = "TEXT")
    private String description;  // 동영상 설명

    // === 파일 정보 ===
    @Column(nullable = false)
    private String originalFileName;  // 원본 파일명 (사용자가 업로드한 이름)

    @Column(nullable = false, unique = true)
    private String storedFileName;  // 저장 파일명 (UUID 등으로 변환된 이름)

    @Column(nullable = false)
    private String filePath;  // 저장 경로

    @Column(nullable = false)
    private Long fileSize;  // 파일 크기 (bytes)

    @Column(length = 50)
    private String mimeType;  // MIME 타입 (video/mp4, video/webm 등)

    @Column(length = 20)
    private String fileExtension;  // 확장자 (.mp4, .avi 등)

    // === 동영상 메타데이터 ===
    private Integer duration;  // 재생 시간 (초 단위)

    private Integer width;  // 가로 해상도

    private Integer height;  // 세로 해상도

    private Integer bitrate;  // 비트레이트 (kbps)

    private String codec;  // 코덱 정보 (H.264, VP9 등)

    // === 썸네일 ===
    private String thumbnailPath;  // 썸네일 이미지 경로

    // === 상태 관리 ===
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private VideoStatus status = VideoStatus.UPLOADED;  // 업로드 상태

    @Builder.Default
    private Long viewCount = 0L;  // 조회수

    @Builder.Default
    private Boolean isPublic = true;  // 공개 여부

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "category_id")
    // private Category category;

    // === 편의 메서드 ===
    public void increaseViewCount() {
        this.viewCount++;
    }

    public String getFormattedDuration() {
        if (duration == null) return "00:00";
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String getFormattedFileSize() {
        if (fileSize == null) return "0 B";

        double size = fileSize;
        String[] units = {"B", "KB", "MB", "GB"};
        int unitIndex = 0;

        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        return String.format("%.1f %s", size, units[unitIndex]);
    }
}
