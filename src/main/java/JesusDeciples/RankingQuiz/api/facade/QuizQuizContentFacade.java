package JesusDeciples.RankingQuiz.api.facade;

import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.service.quiz.QuizService;
import JesusDeciples.RankingQuiz.api.service.quizContent.QuizContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizQuizContentFacade {
    private final QuizService quizService;
    private final QuizContentService quizContentService;

    public Quiz setNewQuizExcept(Long presentQuizContentId) {
        QuizContent quizContentEntity = quizContentService.getQuizContentExcept(presentQuizContentId);
        return quizService.addNewQuiz(quizContentEntity);
    }

    public Quiz setNewQuiz() {
        QuizContent quizContentEntity = quizContentService.getRandomQuizContent();
        return quizService.addNewQuiz(quizContentEntity);
    }
}
