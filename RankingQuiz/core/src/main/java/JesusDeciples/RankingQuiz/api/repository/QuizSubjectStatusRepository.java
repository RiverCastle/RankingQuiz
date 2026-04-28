package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.QuizSubjectStatus;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizSubjectStatusRepository extends JpaRepository<QuizSubjectStatus, QuizCategory> {
}
