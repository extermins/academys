package minjae.academy.banner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerDto {
    private UUID uuid;
    private String title;
    private String description;
    private String url;
    private String imagePath;

}
