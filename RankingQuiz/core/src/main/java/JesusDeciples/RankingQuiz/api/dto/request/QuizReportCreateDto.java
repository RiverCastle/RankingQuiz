package JesusDeciples.RankingQuiz.api.dto.request;

import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import lombok.Data;

@Data
public class QuizReportCreateDto {
    private Long quizId;
    private QuizCategory category;
    private String question;
    private String originalAnswer;
    private String userAnswer;
    private String reporterId;
}
