package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MultipleChoiceQuizContentRepository extends JpaRepository<MultipleChoiceQuizContent, Long> {

    @Query("Select mc From MultipleChoiceQuizContent mc Join Fetch mc.options Where mc.id = :id")
    Optional<MultipleChoiceQuizContent> findByIdWithOptions(@Param("id") Long id);

    @Query("SELECT MAX(e.id) FROM MultipleChoiceQuizContent e")
    Long findMaxId();

    @Query("SELECT MIN(e.id) FROM MultipleChoiceQuizContent e")
    Long findMinId();

}
