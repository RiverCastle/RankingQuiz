package JesusDeciples.RankingQuiz.api.webSocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final QuizHandlerRegistry handlerRegistry;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(routingHandler(), "/api/ws/quiz/**")
                .setAllowedOrigins("http://localhost:8081", "https://rankingquiz.rivercastleworks.site");
    }

    @Bean
    public WebSocketHandler routingHandler() {
        return new WebSocketHandler() {

            private String extractCategoryCode(WebSocketSession session) {
                String path = session.getUri().getPath();
                int lastSlash = path.lastIndexOf('/');
                if (lastSlash < 0 || lastSlash >= path.length() - 1) return null;
                return path.substring(lastSlash + 1).toUpperCase();
            }

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                String code = extractCategoryCode(session);
                if (code == null) {
                    session.close(CloseStatus.BAD_DATA);
                    return;
                }
                handlerRegistry.getOrCreate(code).afterConnectionEstablished(session);
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                String code = extractCategoryCode(session);
                if (code != null) {
                    handlerRegistry.getOrCreate(code).handleMessage(session, message);
                }
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                String code = extractCategoryCode(session);
                if (code != null) {
                    handlerRegistry.getOrCreate(code).handleTransportError(session, exception);
                }
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
                String code = extractCategoryCode(session);
                if (code != null) {
                    handlerRegistry.getOrCreate(code).afterConnectionClosed(session, status);
                }
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };
    }
}
