package minjae.academy.banner.entity;

import jakarta.persistence.*;
import lombok.*;
import minjae.academy.config.JpaAuditing.BaseEntity;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Banner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column
    private String description;

    @Column(length = 500)
    private String linkUrl;

    @Column(length = 500, nullable = false)
    private String imageUrl;

    //생성 될 때에는 비활성화가 기본 값으로 되도록 해주는게 관리면에서 나을것으로 생각됨
    //바로 나오는거는 아닌듯
    @Column(nullable = false)
    private boolean enabled;

    @Column(unique = true)
    private Integer displayOrder;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    public boolean isDisplayable() {
        if (!enabled) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        boolean afterStart = (startDate == null) || !now.isBefore(startDate);
        boolean beforeEnd = (endDate == null) || !now.isAfter(endDate);

        return afterStart && beforeEnd;
    }

    public boolean isScheduled() {
        return startDate != null && LocalDateTime.now().isBefore(startDate);
    }

    public boolean isExpired() {
        return endDate != null && LocalDateTime.now().isAfter(endDate);
    }
}
