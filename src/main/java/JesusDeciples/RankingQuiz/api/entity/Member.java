package JesusDeciples.RankingQuiz.api.entity;

import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.enums.Authority;
import JesusDeciples.RankingQuiz.api.enums.OAuthProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotBlank
    private String email;
    @Enumerated(EnumType.STRING)
    private Authority authority;
    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;

    private Integer point;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private Map<Quiz, QuizResult> quizResults = new HashMap<>();
}
