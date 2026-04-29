package JesusDeciples.RankingQuiz.api.service.quizContent;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ReferenceTag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface QuizContentService {
    void addQuizContent(QuizContentCreateDto quizContentCreateDto);
    QuizContent getQuizContentExcept(Long presentQuizContentId, String categoryCode);
    QuizContent getRandomQuizContent(String categoryCode);
    void saveToRepository(QuizContent entity);
    QuizContent getQuizContentById(Long id);
    List<QuizContent> findAllByReferenceTagIn(Set<ReferenceTag> tagSet);
}
