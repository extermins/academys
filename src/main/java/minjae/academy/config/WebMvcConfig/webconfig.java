package minjae.academy.config.WebMvcConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webconfig implements WebMvcConfigurer {
    @Value("${file.upload.directory}")
    private String directory;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 업로드된 이미지 파일 접근 경로 설정
        registry.addResourceHandler("/uploads/banners/**")
                .addResourceLocations("file:" + directory + "/banner/");

        registry.addResourceHandler("/uploads/video/**")
                .addResourceLocations("file:" + directory + "/video/");
        // 또는 절대 경로로 직접 지정
        // registry.addResourceHandler("/uploads/**")
        //         .addResourceLocations("file:///C:/uploads/");
    }
}
