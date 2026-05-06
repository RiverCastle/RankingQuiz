package JesusDeciples.RankingQuiz.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizReportDto {
    private Long quizId;
    private String category;
    private String question;
    private String answer;
    private String myAnswer;
    private String reporterId;
    private LocalDateTime reportedAt;
}
