package JesusDeciples.RankingQuiz.api.service.quizContent;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import org.springframework.stereotype.Service;

@Service
public interface QuizContentService {
    void addQuiz(QuizContentCreateDto quizContentCreateDto);
    QuizContent getQuizContentExcept(Long presentQuizContentId);

    QuizContent getRandomQuizContent();
}
