package JesusDeciples.RankingQuiz.api.service.quiz;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface QuizService {
    QuizResponseDto addNewQuiz(QuizType quizType);
}
