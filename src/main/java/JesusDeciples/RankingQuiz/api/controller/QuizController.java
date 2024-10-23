package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResponseDto;
import JesusDeciples.RankingQuiz.api.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    private QuizResponseDto presentQuiz;
    @GetMapping
    public QuizResponseDto getQuizResponse() {
        return this.presentQuiz;
    }

    @PostMapping
    public void setPresentQuiz(QuizType quizType) {
        this.presentQuiz = quizService.addNewQuiz(quizType);
    }


}
