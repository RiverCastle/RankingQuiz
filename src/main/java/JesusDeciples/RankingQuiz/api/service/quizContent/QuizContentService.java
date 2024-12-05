package JesusDeciples.RankingQuiz.api.service.quizContent;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import org.springframework.stereotype.Service;

@Service
public interface QuizContentService {
    void addQuizContent(QuizContentCreateDto quizContentCreateDto);
    QuizContent getQuizContentExcept(Long presentQuizContentId, QuizCategory category);

    QuizContent getRandomQuizContent(QuizCategory category);
}
