package JesusDeciples.RankingQuiz.api.entity.quizContent;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class MultipleChoiceQuizContent extends QuizContent {
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> options;
}
