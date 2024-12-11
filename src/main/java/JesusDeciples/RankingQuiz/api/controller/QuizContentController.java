package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizContentReviewDto;
import JesusDeciples.RankingQuiz.api.facade.QuizContentCreateFacade;
import JesusDeciples.RankingQuiz.api.facade.QuizContentReadFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz-content")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "QuizContent API", description = "퀴즈 문항 API 입니다.")
public class QuizContentController {
    private final QuizContentCreateFacade quizContentCreateFacade;
    private final QuizContentReadFacade readFacade;
    @PostMapping
    @Operation(summary = "QuizContent 등록",
            description = "주어진 QuizContentCreateDto 배열을 사용하여 퀴즈 콘텐츠를 등록합니다.")
    public void addQuizContent(@RequestBody QuizContentCreateDto[] quizContentCreateDtos) {
        for (QuizContentCreateDto quizContentCreateDto : quizContentCreateDtos)
            quizContentCreateFacade.createQuizContent(quizContentCreateDto);
    }

    @GetMapping("/review-content/{quizContentId}")
    @Operation(summary = "복습용 QuizContent 조회",
            description = "주어진 퀴즈 콘텐츠 ID에 연결된 리뷰 DTO 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "quizContentId", description = "리뷰를 조회할 퀴즈 콘텐츠의 ID", required = true, example = "1")
            })
    public ResponseEntity<List<QuizContentReviewDto>> getQuizContentForReview(@PathVariable("quizContentId") Long quizContentId) {
        return ResponseEntity.ok(readFacade.readQuizContentLinkedWith(quizContentId));
    }
}
