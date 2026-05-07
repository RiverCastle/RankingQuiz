package JesusDeciples.RankingQuiz.api.entity.quizContent;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class ImageQuizContent extends QuizContent {

    private String imageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> options;
}
