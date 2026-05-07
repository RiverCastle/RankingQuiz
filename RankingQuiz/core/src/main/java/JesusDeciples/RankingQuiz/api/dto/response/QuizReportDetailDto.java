package JesusDeciples.RankingQuiz.api.dto.response;

import JesusDeciples.RankingQuiz.api.entity.QuizReport;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.enums.ReportStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuizReportDetailDto {
    private final Long id;
    private final Long quizId;
    private final QuizCategory category;
    private final String question;
    private final String originalAnswer;
    private final String userAnswer;
    private final String reporterId;
    private final LocalDateTime reportedAt;
    private final ReportStatus status;

    private QuizReportDetailDto(QuizReport report) {
        this.id = report.getId();
        this.quizId = report.getQuizId();
        this.category = report.getCategory();
        this.question = report.getQuestion();
        this.originalAnswer = report.getOriginalAnswer();
        this.userAnswer = report.getUserAnswer();
        this.reporterId = report.getReporterId();
        this.reportedAt = report.getReportedAt();
        this.status = report.getStatus();
    }

    public static QuizReportDetailDto from(QuizReport report) {
        return new QuizReportDetailDto(report);
    }
}
