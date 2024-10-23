package JesusDeciples.RankingQuiz.api.service.quiz;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResponseDto;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import org.springframework.stereotype.Service;

@Service
public interface QuizService {
    QuizResponseDto addNewQuiz(QuizType quizType);
    Quiz getQuiz(Long quizId);
}
