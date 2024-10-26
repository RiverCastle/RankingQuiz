package JesusDeciples.RankingQuiz.api.dto;

import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import jakarta.transaction.Transactional;
import lombok.Data;

import java.util.List;

@Data
public class QuizContentDto {
    private String statement;
    private List<String> options;

    @Transactional
    public QuizContentDto fromEntity(QuizContent entity) {
        QuizContentDto dto = new QuizContentDto();
        dto.setStatement(entity.getStatement());
        if (entity instanceof MultipleChoiceQuizContent) {
            dto.setOptions(((MultipleChoiceQuizContent) entity).getOptions());
        }

        return dto;
    }
}
