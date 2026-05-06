package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.QuizReport;
import JesusDeciples.RankingQuiz.api.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizReportRepository extends JpaRepository<QuizReport, Long> {
    List<QuizReport> findAllByStatusNotOrderByReportedAtDesc(ReportStatus status);
}
