package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.request.QuizReportCreateDto;
import JesusDeciples.RankingQuiz.api.dto.request.ReportStatusUpdateDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizReportDetailDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizReportSummaryDto;
import JesusDeciples.RankingQuiz.api.entity.QuizReport;
import JesusDeciples.RankingQuiz.api.enums.ReportStatus;
import JesusDeciples.RankingQuiz.api.repository.QuizReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizReportServiceImpl implements QuizReportService {

    private final QuizReportRepository quizReportRepository;

    @Override
    @Transactional
    public void createReport(QuizReportCreateDto dto) {
        quizReportRepository.save(QuizReport.from(dto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizReportSummaryDto> getAllActiveReports() {
        return quizReportRepository
                .findAllByStatusNotOrderByReportedAtDesc(ReportStatus.DELETED)
                .stream()
                .map(QuizReportSummaryDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuizReportDetailDto getReportDetail(Long id) {
        QuizReport report = quizReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다. id=" + id));
        return QuizReportDetailDto.from(report);
    }

    @Override
    @Transactional
    public void updateReportStatus(Long id, ReportStatusUpdateDto dto) {
        QuizReport report = quizReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다. id=" + id));
        report.updateStatus(dto.getStatus());
    }
}
