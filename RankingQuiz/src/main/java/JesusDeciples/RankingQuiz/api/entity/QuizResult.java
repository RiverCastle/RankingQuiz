package JesusDeciples.RankingQuiz.api.entity;

import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Quiz quiz;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private String myAnswer;
    private boolean isCorrect;
    private LocalDateTime writtenAt;
}
