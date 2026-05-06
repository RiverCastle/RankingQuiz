package JesusDeciples.RankingQuiz.api.entity;

import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.enums.SuggestionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class QuizSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private QuizCategory category;

    @Column(length = 1000)
    private String statement;

    private String answer;

    @Column(length = 2000)
    private String choices;

    @Enumerated(EnumType.STRING)
    private SuggestionStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = SuggestionStatus.PENDING;
    }

    public void approve() {
        this.status = SuggestionStatus.APPROVED;
    }

    public void reject() {
        this.status = SuggestionStatus.REJECTED;
    }

    public static QuizSuggestion of(QuizCategory category, String statement, String answer, String choicesJson) {
        QuizSuggestion suggestion = new QuizSuggestion();
        suggestion.category = category;
        suggestion.statement = statement;
        suggestion.answer = answer;
        suggestion.choices = choicesJson;
        return suggestion;
    }
}
