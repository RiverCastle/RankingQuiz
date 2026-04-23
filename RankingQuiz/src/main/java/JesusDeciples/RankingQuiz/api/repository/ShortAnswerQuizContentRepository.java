package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.quizContent.ShortAnswerQuizContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortAnswerQuizContentRepository extends JpaRepository<ShortAnswerQuizContent, Long> {
}
