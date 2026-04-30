package JesusDeciples.RankingQuiz.api.dto.response;

import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import lombok.Data;

import java.util.List;

@Data
public class QuizContentReviewDto {
    private Long id;
    private String statement;
    private String answer;
    private String categoryCode;
    private List<String> options;

    public static QuizContentReviewDto of(QuizContent entity) {
        QuizContentReviewDto dto = new QuizContentReviewDto();
        dto.setId(entity.getId());
        dto.setStatement(entity.getStatement());
        dto.setAnswer(entity.getAnswer());
        dto.setCategoryCode(entity.getCategoryCode());
        if (entity instanceof MultipleChoiceQuizContent) {
            dto.setOptions(((MultipleChoiceQuizContent) entity).getOptions());
        }
        return dto;
    }
}
