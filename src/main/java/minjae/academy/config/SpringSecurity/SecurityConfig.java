package minjae.academy.config.SpringSecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors->cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/", "/login", "/signup", "/logout","/error").permitAll()
                        .requestMatchers("/css/**","/js/**", "/images/**","/uploads/**").permitAll()
                        .requestMatchers( "/admin/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/",true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")              // 로그아웃 URL
                        .logoutSuccessUrl("/")        // 로그아웃 성공 시 이동할 URL
                        .invalidateHttpSession(true)       // 세션 삭제
                        .deleteCookies("JSESSIONID")       // 쿠키 삭제
                        .permitAll()
                );

        return http.build();


    }
}
