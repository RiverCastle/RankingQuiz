package JesusDeciples.RankingQuiz.api.entity;

import JesusDeciples.RankingQuiz.api.dto.request.QuizReportCreateDto;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class QuizReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quizId;

    @Enumerated(EnumType.STRING)
    private QuizCategory category;

    @Column(length = 1000)
    private String question;

    private String originalAnswer;

    private String userAnswer;

    private String reporterId;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private LocalDateTime reportedAt;

    @PrePersist
    private void onCreate() {
        this.reportedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = ReportStatus.UNREAD;
        }
    }

    public static QuizReport from(QuizReportCreateDto dto) {
        QuizReport report = new QuizReport();
        report.quizId = dto.getQuizId();
        report.category = dto.getCategory();
        report.question = dto.getQuestion();
        report.originalAnswer = dto.getOriginalAnswer();
        report.userAnswer = dto.getUserAnswer();
        report.reporterId = dto.getReporterId();
        return report;
    }

    public void updateStatus(ReportStatus status) {
        this.status = status;
    }
}
