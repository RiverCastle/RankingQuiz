package JesusDeciples.RankingQuiz.api.entity.quizContent;

import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuizContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String statement;
    private String answer;
    private Integer timeLimit;
    @Enumerated(EnumType.STRING)
    private QuizCategory category;
}