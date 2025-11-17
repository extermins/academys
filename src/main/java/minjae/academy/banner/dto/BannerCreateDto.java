package minjae.academy.banner.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minjae.academy.banner.entity.Banner;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerCreateDto {
    @NotBlank(message = "배너 설명은 필수입니다.")
    @Size(max = 200, message = "배너 설명은 200자를 초과할 수 없습니다.")
    private String description;

    @NotBlank(message = "링크 URL은 필수입니다.")
    @Pattern(regexp = "^(http|https)://.*$", message = "올바른 URL 형식이 아닙니다.")
    @Size(max = 500, message = "URL은 500자를 초과할 수 없습니다.")
    private String linkUrl;

    @NotBlank(message = "이미지 URL은 필수입니다.")
    @Size(max = 500, message = "이미지 URL은 500자를 초과할 수 없습니다.")
    private String imageUrl;

    @NotNull(message = "활성화 여부는 필수입니다.")
    private Boolean enabled;

//    @NotNull(message = "표시 순서는 필수입니다.")
//    @Min(value = 1, message = "표시 순서는 1 이상이어야 합니다.")
//    @Max(value = 100, message = "표시 순서는 100 이하여야 합니다.")
//    private Integer displayOrder;

    private LocalDate startDate;

    private LocalDate endDate;

    public Banner toBanner() {
        return Banner.builder()
                .description(this.description)
                .linkUrl(this.linkUrl)
                .imageUrl(this.imageUrl)
                .enabled(this.enabled)
//                .displayOrder(this.displayOrder)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .build();
    }

    // 커스텀 검증: 종료일이 시작일보다 이후인지 확인
    @AssertTrue(message = "종료일은 시작일보다 이후여야 합니다.")
    public boolean isValidDateRange() {
        if (startDate == null || endDate == null) {
            return true; // null 체크는 @NotNull이 담당
        }
        return endDate.isAfter(startDate);
    }
}
