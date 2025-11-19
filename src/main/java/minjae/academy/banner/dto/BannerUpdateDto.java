package minjae.academy.banner.dto;

import jakarta.persistence.Column;
import lombok.*;
import minjae.academy.banner.entity.Banner;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerUpdateDto {
    //createDto 랑 다른점은 uuid가 존재한다
    private String uuid;

    private String description;

    private String linkUrl;

    private String imageUrl;

    private boolean enabled;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
