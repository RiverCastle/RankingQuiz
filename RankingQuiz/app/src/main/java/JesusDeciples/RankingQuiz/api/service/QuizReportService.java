package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.request.QuizReportCreateDto;
import JesusDeciples.RankingQuiz.api.dto.request.ReportStatusUpdateDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizReportDetailDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizReportSummaryDto;

import java.util.List;

public interface QuizReportService {
    void createReport(QuizReportCreateDto dto);
    List<QuizReportSummaryDto> getAllActiveReports();
    QuizReportDetailDto getReportDetail(Long id);
    void updateReportStatus(Long id, ReportStatusUpdateDto dto);
}
