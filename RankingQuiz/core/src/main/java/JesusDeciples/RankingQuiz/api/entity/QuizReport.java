package JesusDeciples.RankingQuiz.api.entity;

import JesusDeciples.RankingQuiz.api.dto.QuizReportDto;
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

    private String category;

    @Column(length = 1000)
    private String question;

    @Column(length = 500)
    private String answer;

    @Column(length = 500)
    private String myAnswer;

    private String reporterId;

    @Enumerated(EnumType.STRING)
    private QuizReportStatus status;

    private LocalDateTime reportedAt;

    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        this.status = QuizReportStatus.UNREAD;
        this.createdAt = LocalDateTime.now();
    }

    public static QuizReport from(QuizReportDto dto) {
        QuizReport report = new QuizReport();
        report.quizId     = dto.getQuizId();
        report.category   = dto.getCategory();
        report.question   = dto.getQuestion();
        report.answer     = dto.getAnswer();
        report.myAnswer   = dto.getMyAnswer();
        report.reporterId = dto.getReporterId();
        report.reportedAt = dto.getReportedAt();
        return report;
    }
}
