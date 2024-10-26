package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.service.quizContent.QuizContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz-content")
public class QuizContentController {
    private final QuizContentService quizContentService;
    @PostMapping
    public void addQuizContent(@RequestBody QuizContentCreateDto[] quizContentCreateDtos) {
        for (QuizContentCreateDto quizContentCreateDto : quizContentCreateDtos)
        quizContentService.addQuiz(quizContentCreateDto);
    }
}
