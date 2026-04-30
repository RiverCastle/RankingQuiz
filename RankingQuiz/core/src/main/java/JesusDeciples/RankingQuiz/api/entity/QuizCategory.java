package JesusDeciples.RankingQuiz.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz_category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizCategory {

    @Id
    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "allow_multiple_winners", nullable = false)
    private boolean allowMultipleWinners;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static QuizCategory of(String code, String displayName, boolean enabled, boolean allowMultipleWinners) {
        return new QuizCategory(code, displayName, enabled, allowMultipleWinners);
    }
}
