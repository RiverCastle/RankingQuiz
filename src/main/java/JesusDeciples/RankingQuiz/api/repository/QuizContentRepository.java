package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizContentRepository extends JpaRepository<QuizContent, Long> {
    @Query(value = "SELECT * FROM quiz_content qc WHERE qc.category = :category AND qc.id != :id ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<QuizContent> findRandomByIdNotAndCategory(@Param("id") Long id, @Param("category") String category);
    @Query(value = "SELECT * FROM quiz_content qc WHERE qc.category = :category ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<QuizContent> findRandomByCategory(@Param("category") String category);
}
