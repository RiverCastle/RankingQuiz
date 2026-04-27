package JesusDeciples.RankingQuiz.api.entity.quizContent;

import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "quizContent")
    private List<QuizContentLinkReferenceTag> links = new ArrayList<>();
    public void linkWithTags(List<QuizContentLinkReferenceTag> list) {
        this.links = list;
        for (QuizContentLinkReferenceTag classLink : list) {
            classLink.setQuizContent(this);
        }
    }
}