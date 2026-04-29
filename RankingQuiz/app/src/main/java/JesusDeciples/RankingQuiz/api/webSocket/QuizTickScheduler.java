package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.webSocket.messageHandler.WebSocketQuizHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class QuizTickScheduler {

    private final QuizHandlerRegistry handlerRegistry;

    @Scheduled(fixedDelay = 1000)
    public void tick() throws IOException, InterruptedException {
        for (WebSocketQuizHandler handler : handlerRegistry.getAllHandlers()) {
            handler.tick();
        }
    }
}
