package JesusDeciples.RankingQuiz.api.config;

import JesusDeciples.RankingQuiz.api.jwt.JWTFilter;
import JesusDeciples.RankingQuiz.api.jwt.JWTProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/quiz-content").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/admin/quiz/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/quiz-status/public").permitAll()
                        .requestMatchers("/api/quiz-status/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/quiz-sessions/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/admin/quiz/reports/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/member/**").authenticated()
                        .requestMatchers("/api/quiz-results/**").authenticated()
                        .requestMatchers("/api/quiz/reports").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/quiz-suggestions").permitAll()
                        .requestMatchers("/api/quiz-suggestions/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JWTFilter(jwtProducer), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
