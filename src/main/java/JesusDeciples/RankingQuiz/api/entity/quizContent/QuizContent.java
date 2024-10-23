package JesusDeciples.RankingQuiz.api.entity.quizContent;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class QuizContent {
    private String statement;
    private String answer;
    private Integer timeLimit;
}