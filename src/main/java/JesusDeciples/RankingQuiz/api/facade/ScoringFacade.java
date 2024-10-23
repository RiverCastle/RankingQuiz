package JesusDeciples.RankingQuiz.api.facade;

import JesusDeciples.RankingQuiz.api.dto.response.ScoreResponseDto;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScoringFacade {
    private final QuizService quizService;
    public ScoreResponseDto score(Long quizId, String answer) {
        Quiz finishedQuiz = quizService.getQuiz(quizId);
        boolean isCorrect = finishedQuiz.getQuizContent().getAnswer().equals(answer);

        // TODO score save logic
        return new ScoreResponseDto(isCorrect);
    }
}
