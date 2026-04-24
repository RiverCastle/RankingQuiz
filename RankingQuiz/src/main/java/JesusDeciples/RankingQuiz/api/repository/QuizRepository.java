package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("SELECT q FROM Quiz q JOIN FETCH q.quizContent mc WHERE q.id = :id")
    Optional<Quiz> findByIdWithMultipleChoiceQuizContent(@Param("id") Long id);

    @Query("SELECT q FROM Quiz q JOIN FETCH q.quizContent mc WHERE q.id = :id")
    Optional<Quiz> findById(@Param("id") Long id);
}
