package JesusDeciples.RankingQuiz.api.dto.response;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MultipleChoiceQuizResponseDto extends QuizResponseDto {
    private String statement;
    private List<String> options;

}
