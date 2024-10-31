package JesusDeciples.RankingQuiz.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .and()
                .authorizeRequests()
                .requestMatchers("/api/quiz-content").hasRole("ADMIN") // 관리자만 접근 가능
                .anyRequest().permitAll()
                .and()
                .oauth2Login() // OAuth2.0 카카오 로그인 설정
                .loginPage("/oauth2/auth/kakao") // 카카오 로그인 페이지 설정
                .permitAll(); // CSRF 보호 비활성화 (API의 경우 필요에 따라 설정);

        return http.build(); // SecurityFilterChain 객체 반환
    }
}
