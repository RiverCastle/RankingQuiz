package JesusDeciples.RankingQuiz.api.config;

import JesusDeciples.RankingQuiz.api.jwt.JWTProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTProducer jwtProducer;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .cors() // CORS 설정을 활성화
                .and()
                .authorizeRequests()
                .requestMatchers("/api/quiz-content").hasAuthority("ROLE_ADMIN") // 관리자만 접근 가능
                .requestMatchers("/api/member/**").authenticated()
                .requestMatchers("/api/quiz-results/**").authenticated()
                .anyRequest().permitAll()

                .and()
                .oauth2Login() // OAuth2.0 카카오 로그인 설정
                .loginPage("/oauth2/auth/kakao") // 카카오 로그인 페이지 설정
                .permitAll()

                .and()
                .apply(new JWTConfig(jwtProducer));

        return http.build(); // SecurityFilterChain 객체 반환
    }
}
