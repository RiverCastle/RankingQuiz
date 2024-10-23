package JesusDeciples.RankingQuiz.api.entity.quizContent;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;

import java.util.List;

@Entity
@Builder
public class MultipleChoiceQuizContent extends QuizContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    protected List<String> options;
}
