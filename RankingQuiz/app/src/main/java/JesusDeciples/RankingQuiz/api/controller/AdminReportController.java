package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.request.ReportStatusUpdateDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizReportDetailDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizReportSummaryDto;
import JesusDeciples.RankingQuiz.api.service.QuizReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/quiz/reports")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "AdminReport API", description = "퀴즈 문제 신고 관리자 API")
public class AdminReportController {

    private final QuizReportService quizReportService;

    @GetMapping
    @Operation(summary = "신고 목록 조회", description = "삭제되지 않은 신고 내역을 최신순으로 반환합니다.")
    public ResponseEntity<List<QuizReportSummaryDto>> getAllReports() {
        return ResponseEntity.ok(quizReportService.getAllActiveReports());
    }

    @GetMapping("/{id}")
    @Operation(summary = "신고 상세 조회", description = "특정 신고 내역의 상세 정보를 반환합니다.")
    public ResponseEntity<QuizReportDetailDto> getReportDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(quizReportService.getReportDetail(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "신고 상태 변경", description = "신고 상태를 RESOLVED 또는 DELETED로 변경합니다.")
    public ResponseEntity<Void> updateReportStatus(
            @PathVariable("id") Long id,
            @RequestBody ReportStatusUpdateDto dto) {
        quizReportService.updateReportStatus(id, dto);
        return ResponseEntity.ok().build();
    }
}
