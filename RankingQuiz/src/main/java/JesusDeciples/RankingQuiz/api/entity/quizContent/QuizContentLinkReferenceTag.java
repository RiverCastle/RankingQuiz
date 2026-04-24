package JesusDeciples.RankingQuiz.api.entity.quizContent;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class QuizContentLinkReferenceTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private QuizContent quizContent;

    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private ReferenceTag referenceTag;
    public QuizContentLinkReferenceTag(ReferenceTag referenceTag) {
        this.referenceTag = referenceTag;
    }
}
