package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.facade.QuizContentCreateFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz-content")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
public class QuizContentController {
    private final QuizContentCreateFacade quizContentCreateFacade;
    @PostMapping
    public void addQuizContent(@RequestBody QuizContentCreateDto[] quizContentCreateDtos) {
        for (QuizContentCreateDto quizContentCreateDto : quizContentCreateDtos)
            quizContentCreateFacade.createQuizContent(quizContentCreateDto);
    }
}
