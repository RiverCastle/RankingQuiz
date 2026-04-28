package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.webSocket.messageHandler.WebSocketBibleQuizHandler;
import JesusDeciples.RankingQuiz.api.webSocket.messageHandler.WebSocketVocaQuizHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz-sessions")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "QuizSession API", description = "퀴즈 세션 접속자 수 조회 API")
public class SessionCountController {

    private final WebSocketVocaQuizHandler vocaQuizHandler;
    private final WebSocketBibleQuizHandler bibleQuizHandler;

    @GetMapping("/count")
    @Operation(summary = "퀴즈별 현재 접속자 수 조회", description = "카테고리별 WebSocket 세션 수를 반환합니다. ROLE_ADMIN 필요.")
    public ResponseEntity<Map<String, Integer>> getSessionCounts() {
        return ResponseEntity.ok(Map.of(
                "ENG_VOCA", vocaQuizHandler.getSessionCount(),
                "BIBLE", bibleQuizHandler.getSessionCount()
        ));
    }
}
