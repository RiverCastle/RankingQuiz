package JesusDeciples.RankingQuiz.api.admin.controller;

import JesusDeciples.RankingQuiz.api.admin.dto.QuizCategoryCreateDto;
import JesusDeciples.RankingQuiz.api.admin.dto.QuizCategoryResponseDto;
import JesusDeciples.RankingQuiz.api.admin.service.QuizCategoryService;
import JesusDeciples.RankingQuiz.api.entity.QuizCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "QuizCategory API", description = "퀴즈 카테고리 관리 API")
public class QuizCategoryController {

    private final QuizCategoryService quizCategoryService;

    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "전체 퀴즈 카테고리 목록을 반환합니다.")
    public ResponseEntity<List<QuizCategoryResponseDto>> getAllCategories() {
        List<QuizCategoryResponseDto> result = quizCategoryService.getAllCategories().stream()
                .map(QuizCategoryResponseDto::from)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "카테고리 신규 등록", description = "새로운 퀴즈 카테고리를 등록합니다. (관리자 전용)")
    public ResponseEntity<QuizCategoryResponseDto> createCategory(@RequestBody @Valid QuizCategoryCreateDto dto) {
        QuizCategory created = quizCategoryService.createCategory(
                dto.getCode().toUpperCase(),
                dto.getDisplayName(),
                dto.isAllowMultipleWinners()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(QuizCategoryResponseDto.from(created));
    }

    @PutMapping("/{code}/status")
    @Operation(summary = "카테고리 활성화/비활성화", description = "특정 카테고리의 활성화 상태를 변경합니다. (관리자 전용)")
    public ResponseEntity<Void> setStatus(
            @PathVariable("code") String code,
            @RequestParam("enabled") boolean enabled) {
        quizCategoryService.setStatus(code.toUpperCase(), enabled);
        return ResponseEntity.ok().build();
    }
}
