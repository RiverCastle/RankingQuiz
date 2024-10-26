package JesusDeciples.RankingQuiz.api.dto.request;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import lombok.Data;

import java.util.List;

@Data
public class QuizContentCreateDto {
    private String statement;
    private Integer timeLimit;
    private String answer;
    private QuizType quizType;
    private List<String> multipleOptions;

    public Integer getTimeLimit() {
        return (timeLimit != null) ? timeLimit : 10;
    }
}
