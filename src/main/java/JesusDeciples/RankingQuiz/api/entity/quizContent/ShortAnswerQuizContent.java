package JesusDeciples.RankingQuiz.api.entity.quizContent;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class ShortAnswerQuizContent extends QuizContent {
    @Id
    @GeneratedValue
    private Long id;
}
