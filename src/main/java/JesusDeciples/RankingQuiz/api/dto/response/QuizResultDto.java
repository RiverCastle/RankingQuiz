package JesusDeciples.RankingQuiz.api.dto.response;

import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class QuizResultDto {
    private boolean isCorrect;
    @Setter
    private boolean haveAnswered;
    private String answer;
    private String myAnswer;
    private String statement;
    private LocalDateTime writtenAt;
    private String userName;

    public QuizResultDto(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public QuizResultDto() {
    }

    public static class setHaveAnswered extends QuizResultDto {
        public setHaveAnswered(boolean b) {
        }
    }
}
