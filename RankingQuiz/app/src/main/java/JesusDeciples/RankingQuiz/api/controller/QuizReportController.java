package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.request.QuizReportCreateDto;
import JesusDeciples.RankingQuiz.api.service.QuizReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz/reports")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "QuizReport API", description = "퀴즈 문제 신고 API")
public class QuizReportController {

    private final QuizReportService quizReportService;

    @PostMapping
    @Operation(summary = "퀴즈 문제 신고", description = "사용자가 퀴즈 문제의 이상을 신고합니다.")
    public ResponseEntity<Void> createReport(@RequestBody QuizReportCreateDto dto) {
        quizReportService.createReport(dto);
        return ResponseEntity.ok().build();
    }
}
