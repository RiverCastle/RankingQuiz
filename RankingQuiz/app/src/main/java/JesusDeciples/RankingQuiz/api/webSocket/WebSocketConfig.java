package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.admin.service.QuizStatusService;
import JesusDeciples.RankingQuiz.api.dto.GuideMessageBundle;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.webSocket.messageHandler.AccessTokenMessageHandler;
import JesusDeciples.RankingQuiz.api.webSocket.messageHandler.WebSocketQuizHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Map;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final CustomTextMessageFactory textMessageFactory;
    private final QuizDataCenterMediator quizDataCenterMediator;
    private final GuideMessageBundle guideMessageBundle;
    private final ObjectMapper objectMapper;
    private final AccessTokenMessageHandler accessTokenMessageHandler;
    private final QuizStatusService quizStatusService;

    @Bean
    public WebSocketQuizHandler vocaQuizHandler() {
        return new WebSocketQuizHandler(
                QuizCategory.ENG_VOCA,
                textMessageFactory,
                quizDataCenterMediator,
                guideMessageBundle,
                objectMapper,
                accessTokenMessageHandler,
                quizStatusService);
    }

    @Bean
    public WebSocketQuizHandler bibleQuizHandler() {
        return new WebSocketQuizHandler(
                QuizCategory.BIBLE,
                textMessageFactory,
                quizDataCenterMediator,
                guideMessageBundle,
                objectMapper,
                accessTokenMessageHandler,
                quizStatusService);
    }

    @Bean
    public Map<QuizCategory, WebSocketQuizHandler> quizHandlerRegistry(
            WebSocketQuizHandler vocaQuizHandler,
            WebSocketQuizHandler bibleQuizHandler) {
        return Map.of(
                QuizCategory.ENG_VOCA, vocaQuizHandler,
                QuizCategory.BIBLE, bibleQuizHandler);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(vocaQuizHandler(), "/api/ws/quiz/voca")
                .addHandler(bibleQuizHandler(), "/api/ws/quiz/bible")
                .setAllowedOrigins("http://localhost:8081", "https://rankingquiz.rivercastleworks.site");
    }
}
