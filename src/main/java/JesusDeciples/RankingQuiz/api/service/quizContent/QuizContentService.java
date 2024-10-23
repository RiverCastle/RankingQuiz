package JesusDeciples.RankingQuiz.api.service.quizContent;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import org.springframework.stereotype.Service;

@Service
public interface QuizContentService {
    void addQuiz(QuizContentCreateDto quizContentCreateDto);
}
