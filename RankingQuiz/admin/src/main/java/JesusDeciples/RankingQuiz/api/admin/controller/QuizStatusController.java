package JesusDeciples.RankingQuiz.api.admin.controller;

import JesusDeciples.RankingQuiz.api.admin.service.QuizStatusService;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz-status")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "QuizStatus API", description = "퀴즈 활성화 상태 관리 API")
public class QuizStatusController {

    private final QuizStatusService quizStatusService;

    @GetMapping
    @Operation(summary = "전체 퀴즈 상태 조회", description = "모든 카테고리의 퀴즈 활성화 상태를 반환합니다.")
    public ResponseEntity<Map<QuizCategory, Boolean>> getAllStatus() {
        return ResponseEntity.ok(quizStatusService.getAllStatus());
    }

    @GetMapping("/public")
    @Operation(summary = "퀴즈 상태 공개 조회", description = "인증 없이 모든 카테고리의 퀴즈 활성화 상태를 반환합니다.")
    public ResponseEntity<Map<QuizCategory, Boolean>> getPublicStatus() {
        return ResponseEntity.ok(quizStatusService.getAllStatus());
    }

    @PutMapping("/{category}")
    @Operation(summary = "퀴즈 상태 변경", description = "특정 카테고리 퀴즈의 활성화 상태를 변경합니다.")
    public ResponseEntity<Void> setStatus(
            @PathVariable("category") QuizCategory category,
            @RequestParam("enabled") boolean enabled) {
        quizStatusService.setStatus(category, enabled);
        return ResponseEntity.ok().build();
    }
}
