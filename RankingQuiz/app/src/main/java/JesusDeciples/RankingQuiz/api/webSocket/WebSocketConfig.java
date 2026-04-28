package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.webSocket.messageHandler.WebSocketBibleQuizHandler;
import JesusDeciples.RankingQuiz.api.webSocket.messageHandler.WebSocketVocaQuizHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketBibleQuizHandler bibleQuizHandler;
    private final WebSocketVocaQuizHandler vocaQuizHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(vocaQuizHandler, "/api/ws/quiz/voca")
                .addHandler(bibleQuizHandler, "/api/ws/quiz/bible")
                .setAllowedOrigins("http://localhost:8081", "https://rankingquiz.rivercastleworks.site"); // CORS 설정
    }
}