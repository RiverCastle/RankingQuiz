package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.response.ImageQuizResponseDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ImageQuizContent;
import JesusDeciples.RankingQuiz.api.service.ImageQuizContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "Quiz API", description = "퀴즈 상세 조회 API")
public class QuizDetailController {

    private final ImageQuizContentService imageQuizContentService;

    @GetMapping("/{id}")
    @Operation(summary = "퀴즈 상세 조회", description = "퀴즈 ID로 상세 정보를 조회합니다. 현재 이미지 퀴즈 유형을 지원합니다.")
    public ResponseEntity<ImageQuizResponseDto> getQuiz(@PathVariable Long id) {
        ImageQuizContent entity = imageQuizContentService.findById(id);
        return ResponseEntity.ok(ImageQuizResponseDto.fromEntity(entity));
    }
}
