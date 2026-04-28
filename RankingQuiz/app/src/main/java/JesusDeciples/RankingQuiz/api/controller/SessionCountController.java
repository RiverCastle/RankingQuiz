package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.webSocket.messageHandler.WebSocketQuizHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz-sessions")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "QuizSession API", description = "퀴즈 세션 접속자 수 조회 API")
public class SessionCountController {

    private final Map<QuizCategory, WebSocketQuizHandler> quizHandlerRegistry;

    @GetMapping("/count")
    @Operation(summary = "퀴즈별 현재 접속자 수 조회", description = "카테고리별 WebSocket 세션 수를 반환합니다. ROLE_ADMIN 필요.")
    public ResponseEntity<Map<String, Integer>> getSessionCounts() {
        Map<String, Integer> counts = quizHandlerRegistry.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().name(),
                        e -> e.getValue().getSessionCount()));
        return ResponseEntity.ok(counts);
    }
}
