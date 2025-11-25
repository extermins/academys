//package minjae.academy.video.entity;
//
//import jakarta.persistence.*;
//
//@Entity
//public class Video {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, length = 200)
//    private String title;  // 동영상 제목
//
//    @Column(columnDefinition = "TEXT")
//    private String description;  // 동영상 설명
//
//    @Column(name = "original_filename", nullable = false)
//    private String originalFilename;  // 원본 파일명
//
//    @Column(name = "stored_filename", nullable = false)
//    private String storedFilename;  // 저장된 파일명 (UUID 등)
//
//    @Column(name = "file_path", nullable = false)
//    private String filePath;  // 파일 저장 경로
//
//    @Column(name = "file_size")
//    private Long fileSize;  // 파일 크기 (bytes)
//
//    @Column(name = "content_type", length = 100)
//    private String contentType;  // MIME 타입 (video/mp4 등)
//
//    @Column(name = "duration")
//    private Integer duration;  // 영상 길이 (초 단위)
//
//    @Column(name = "thumbnail_path")
//    private String thumbnailPath;  // 썸네일 이미지 경로
//
//    @Enumerated(EnumType.STRING)
//    @Column(length = 20)
//    @Builder.Default
//    private VideoStatus status = VideoStatus.ACTIVE;  // 상태
//
//}
