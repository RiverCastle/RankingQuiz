package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.QuizSuggestion;
import JesusDeciples.RankingQuiz.api.enums.SuggestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizSuggestionRepository extends JpaRepository<QuizSuggestion, Long> {
    List<QuizSuggestion> findByStatusOrderByCreatedAtDesc(SuggestionStatus status);
}
