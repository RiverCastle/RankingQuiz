package JesusDeciples.RankingQuiz.api.service.quizContent;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ReferenceTag;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface QuizContentService {
    void addQuizContent(QuizContentCreateDto quizContentCreateDto);
    QuizContent getQuizContentExcept(Long presentQuizContentId, QuizCategory category);
    QuizContent getRandomQuizContent(QuizCategory category);
    void saveToRepository(QuizContent entity);
    QuizContent getQuizContentById(Long id);
    List<QuizContent> findAllByReferenceTagIn(Set<ReferenceTag> tagSet);

    Page<QuizContent> getQuizContentPage(QuizCategory category, String answer, Pageable pageable);
}
