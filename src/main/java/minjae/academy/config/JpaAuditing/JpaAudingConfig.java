package minjae.academy.config.JpaAuditing;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class JpaAudingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("systemdadasd");
    }

}
