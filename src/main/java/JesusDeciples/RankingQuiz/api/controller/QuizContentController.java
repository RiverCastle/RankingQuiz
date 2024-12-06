package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizContentReviewDto;
import JesusDeciples.RankingQuiz.api.facade.QuizContentCreateFacade;
import JesusDeciples.RankingQuiz.api.facade.QuizContentReadFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz-content")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
public class QuizContentController {
    private final QuizContentCreateFacade quizContentCreateFacade;
    private final QuizContentReadFacade readFacade;
    @PostMapping
    public void addQuizContent(@RequestBody QuizContentCreateDto[] quizContentCreateDtos) {
        for (QuizContentCreateDto quizContentCreateDto : quizContentCreateDtos)
            quizContentCreateFacade.createQuizContent(quizContentCreateDto);
    }

    @GetMapping("/review-content/{quizContentId}")
    public ResponseEntity<List<QuizContentReviewDto>> getQuizContentForReview(@PathVariable("quizContentId") Long quizContentId) {
        return ResponseEntity.ok(readFacade.readQuizContentLinkedWith(quizContentId));
    }
}
