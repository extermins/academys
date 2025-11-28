package minjae.academy.config.JpaAuditing;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    //primary_key uuid 사용을 하기 때문에 문자열의 길이를 36으로 설정
    @CreatedBy
    @Column(name = "createdBy", updatable = false, length = 36)
    private String createdBy;

    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "modifiedBy", length = 36)
    private String modifiedBy;

    @Column(name = "modifiedAt")
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Column(name = "deletedBy", length = 36 )
    private String deletedBy;
    @Column(name = "deleted")
    private Boolean deleted = false;
    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    //호출시에 활용 말이 삭제지 나중에 복원 가능하도록 deleted 값을 boolean으로 두어 구분만 해두도록 함
    public void deleted(UUID deletedBy) {
        this.deleted = true;
        this.deletedBy = deletedBy.toString();
        this.deletedAt = LocalDateTime.now();
    }
}
