package JesusDeciples.RankingQuiz.api.entity.quiz;

import JesusDeciples.RankingQuiz.api.entity.BaseTimeEntity;
import JesusDeciples.RankingQuiz.api.entity.QuizResult;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Quiz extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private QuizContent quizContent;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    List<QuizResult> quizResults;

    public Quiz(QuizContent quizContent) {
        this.quizContent = quizContent;
        this.setCreatedAt();
        this.setFinishedAt(quizContent.getTimeLimit());
    }
    public Quiz() {
    }
}
