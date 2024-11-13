package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizContentRepository extends JpaRepository<QuizContent, Long> {
    @Query("SELECT MAX(qc.id) FROM QuizContent qc")
    Long findMaxId();

    @Query("SELECT MIN(qc.id) FROM QuizContent qc")
    Long findMinId();

    Optional<QuizContent> findByIdAndCategory(Long randomId, QuizCategory category);
}
