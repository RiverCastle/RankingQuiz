package JesusDeciples.RankingQuiz.api.dto;

import JesusDeciples.RankingQuiz.api.entity.quizContent.ImageQuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import jakarta.transaction.Transactional;
import lombok.Data;

import java.util.List;

@Data
public class QuizContentDto {
    private String statement;
    private List<String> options;
    private String imageUrl;

    @Transactional
    public QuizContentDto fromEntity(QuizContent entity) {
        QuizContentDto dto = new QuizContentDto();
        dto.setStatement(entity.getStatement());
        if (entity instanceof MultipleChoiceQuizContent mc) {
            dto.setOptions(mc.getOptions());
        } else if (entity instanceof ImageQuizContent img) {
            dto.setOptions(img.getOptions());
            dto.setImageUrl(img.getImageUrl());
        }
        return dto;
    }
}
