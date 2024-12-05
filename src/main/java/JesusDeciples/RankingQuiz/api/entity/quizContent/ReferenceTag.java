package JesusDeciples.RankingQuiz.api.entity.quizContent;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class ReferenceTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;

    @OneToMany(mappedBy = "referenceTag")
    private List<QuizContentLinkReferenceTag> links;


    public ReferenceTag(String tag) {
        this.tag = tag;
    }
}
