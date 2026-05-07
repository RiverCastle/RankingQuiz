package JesusDeciples.RankingQuiz.api.dto.response;

import JesusDeciples.RankingQuiz.api.entity.QuizReport;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.enums.ReportStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuizReportSummaryDto {
    private final Long id;
    private final Long quizId;
    private final QuizCategory category;
    private final String question;
    private final String reporterId;
    private final ReportStatus status;
    private final LocalDateTime reportedAt;

    private QuizReportSummaryDto(QuizReport report) {
        this.id = report.getId();
        this.quizId = report.getQuizId();
        this.category = report.getCategory();
        this.question = report.getQuestion();
        this.reporterId = report.getReporterId();
        this.status = report.getStatus();
        this.reportedAt = report.getReportedAt();
    }

    public static QuizReportSummaryDto from(QuizReport report) {
        return new QuizReportSummaryDto(report);
    }
}
