package JesusDeciples.RankingQuiz.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads/quiz-images}")
    private String uploadDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081", "https://rankingquiz.rivercastleworks.site")
                .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Windows 경로(역슬래시)를 file:/// URI로 변환해야 Spring이 올바르게 인식
        String uriBase = Paths.get(uploadDir).toAbsolutePath().normalize().toUri().toString();
        if (!uriBase.endsWith("/")) uriBase = uriBase + "/";
        registry.addResourceHandler("/uploads/quiz-images/**")
                .addResourceLocations(uriBase);
    }
}
