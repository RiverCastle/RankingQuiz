package JesusDeciples.RankingQuiz.api.dto.response;

import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.List;

@Data
public class QuizContentReviewDto {
    private Long id;
    private String statement;
    private String answer;
    @Enumerated(EnumType.STRING)
    private QuizCategory category;
    private List<String> options;

    public static QuizContentReviewDto of(QuizContent entity) {
        QuizContentReviewDto dto = new QuizContentReviewDto();
        dto.setId(entity.getId());
        dto.setStatement(entity.getStatement());
        dto.setAnswer(entity.getAnswer());
        if (entity instanceof MultipleChoiceQuizContent) {
            dto.setOptions(((MultipleChoiceQuizContent) entity).getOptions());
        }
        return dto;
    }
}
