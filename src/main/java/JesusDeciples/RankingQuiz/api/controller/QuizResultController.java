package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.security.SecurityUtil;
import JesusDeciples.RankingQuiz.api.service.QuizResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz-results")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
public class QuizResultController {
    private final QuizResultService quizResultService;

    @GetMapping("/my-results")
    public ResponseEntity<List<QuizResultDto>> getMyQuizResults() {
        return ResponseEntity.ok(quizResultService.getMyQuizResults(SecurityUtil.getCurrentMemberId()));
    }
}
