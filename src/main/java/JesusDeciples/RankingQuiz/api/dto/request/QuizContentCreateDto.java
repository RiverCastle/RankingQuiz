package JesusDeciples.RankingQuiz.api.dto.request;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizContentCreateDto {
    @NotBlank
    private String statement;
    private Integer timeLimit;
    @NotBlank
    private String answer;
    @NotBlank
    private QuizType quizType;
    private List<String> multipleOptions;
    @NotBlank
    private QuizCategory category;
    private List<String> tags;
    public Integer getTimeLimit() {
        return (timeLimit != null) ? timeLimit : 10;
    }
}
