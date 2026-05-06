package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.request.QuizSuggestionCreateDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizSuggestionResponseDto;
import JesusDeciples.RankingQuiz.api.service.QuizSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz-suggestions")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "QuizSuggestion API", description = "사용자 문제 제안 API 입니다.")
public class QuizSuggestionController {

    private final QuizSuggestionService suggestionService;

    @PostMapping
    @Operation(summary = "문제 제안 신청", description = "사용자가 작성한 문제를 제안 테이블에 저장합니다.")
    public ResponseEntity<Void> submitSuggestion(@RequestBody @Valid QuizSuggestionCreateDto dto) {
        suggestionService.submitSuggestion(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "제안 목록 조회 (관리자)", description = "대기 중인 제안 목록을 조회합니다.")
    public ResponseEntity<List<QuizSuggestionResponseDto>> getPendingSuggestions() {
        return ResponseEntity.ok(suggestionService.getPendingSuggestions());
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "제안 승인 (관리자)", description = "제안을 승인하여 실제 퀴즈 테이블에 문제를 생성합니다.")
    public ResponseEntity<Void> approveSuggestion(@PathVariable Long id) {
        suggestionService.approveSuggestion(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "제안 반려 (관리자)", description = "제안을 반려하고 상태를 변경합니다.")
    public ResponseEntity<Void> rejectSuggestion(@PathVariable Long id) {
        suggestionService.rejectSuggestion(id);
        return ResponseEntity.ok().build();
    }
}
