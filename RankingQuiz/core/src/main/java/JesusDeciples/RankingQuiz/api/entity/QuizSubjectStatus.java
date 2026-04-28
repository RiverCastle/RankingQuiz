package JesusDeciples.RankingQuiz.api.entity;

import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubjectStatus {

    @Id
    @Enumerated(EnumType.STRING)
    private QuizCategory category;

    private boolean enabled;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
