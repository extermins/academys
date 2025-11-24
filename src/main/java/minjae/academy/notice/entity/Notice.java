package minjae.academy.notice.entity;

import jakarta.persistence.*;
import lombok.*;
import minjae.academy.config.JpaAuditing.BaseEntity;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;   // Markdown 저장

    private Integer viewCount = 0;


}
