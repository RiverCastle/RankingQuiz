package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.QuizReportDto;
import JesusDeciples.RankingQuiz.api.service.quizReport.QuizReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "QuizReport API", description = "퀴즈 문제 이상 신고 API 입니다.")
public class QuizReportController {

    private final QuizReportService quizReportService;

    @PostMapping("/reports")
    @Operation(summary = "문제 이상 신고 접수",
            description = "사용자가 퀴즈 문제의 이상을 신고합니다. 퀴즈 ID, 카테고리, 문제 내용이 DB에 저장됩니다.")
    public ResponseEntity<Void> reportQuizIssue(@RequestBody QuizReportDto dto) {
        quizReportService.save(dto);
        return ResponseEntity.ok().build();
    }
}
