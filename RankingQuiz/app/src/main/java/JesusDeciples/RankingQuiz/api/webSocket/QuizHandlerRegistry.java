package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.admin.service.QuizCategoryService;
import JesusDeciples.RankingQuiz.api.dto.GuideMessageBundle;
import JesusDeciples.RankingQuiz.api.webSocket.messageHandler.AccessTokenMessageHandler;
import JesusDeciples.RankingQuiz.api.webSocket.messageHandler.WebSocketQuizHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuizHandlerRegistry {

    private final ConcurrentHashMap<String, WebSocketQuizHandler> handlers = new ConcurrentHashMap<>();

    private final CustomTextMessageFactory textMessageFactory;
    private final QuizDataCenterMediator quizDataCenterMediator;
    private final GuideMessageBundle guideMessageBundle;
    private final ObjectMapper objectMapper;
    private final AccessTokenMessageHandler accessTokenMessageHandler;
    private final QuizCategoryService quizCategoryService;

    public WebSocketQuizHandler getOrCreate(String categoryCode) {
        return handlers.computeIfAbsent(categoryCode, code ->
                new WebSocketQuizHandler(
                        code,
                        textMessageFactory,
                        quizDataCenterMediator,
                        guideMessageBundle,
                        objectMapper,
                        accessTokenMessageHandler,
                        quizCategoryService));
    }

    public Collection<WebSocketQuizHandler> getAllHandlers() {
        return handlers.values();
    }

    public Map<String, Integer> getSessionCounts() {
        return handlers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getSessionCount()));
    }
}
