package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ShortAnswerQuizContent;
import org.springframework.stereotype.Service;

@Service
public interface ShortAnswerQuizContentService {
    ShortAnswerQuizContent createShortAnswerQuizContent(QuizContentCreateDto dto);

    void saveToRepository(ShortAnswerQuizContent entity);
}
