package JesusDeciples.RankingQuiz.api.facade;

import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.service.quiz.QuizService;
import JesusDeciples.RankingQuiz.api.service.quizContent.QuizContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizQuizContentFacade {
    private final QuizService quizService;
    private final QuizContentService quizContentService;

    public Quiz setNewQuizExcept(Long presentQuizContentId, QuizCategory quizCategory) {
        QuizContent quizContentEntity = quizContentService.getQuizContentExcept(presentQuizContentId, quizCategory);
        return quizService.addNewQuiz(quizContentEntity);
    }

    public Quiz setNewQuiz(QuizCategory quizCategory) {
        QuizContent quizContentEntity = quizContentService.getRandomQuizContent(quizCategory);
        return quizService.addNewQuiz(quizContentEntity);
    }
}
