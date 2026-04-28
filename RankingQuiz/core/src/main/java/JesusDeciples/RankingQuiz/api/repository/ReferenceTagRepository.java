package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.quizContent.ReferenceTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ReferenceTagRepository extends JpaRepository<ReferenceTag, Long> {
    List<ReferenceTag> findAllByTagIn(Set<String> tagSet);
}
