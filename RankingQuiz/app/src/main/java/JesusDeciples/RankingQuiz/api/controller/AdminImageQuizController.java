package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.response.ImageQuizResponseDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ImageQuizContent;
import JesusDeciples.RankingQuiz.api.service.FileStorageService;
import JesusDeciples.RankingQuiz.api.service.ImageQuizContentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/quiz/image")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "Admin Image Quiz API", description = "이미지 퀴즈 관리 API")
public class AdminImageQuizController {

    private final ImageQuizContentService imageQuizContentService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "이미지 퀴즈 등록", description = "이미지 파일과 함께 새로운 이미지 퀴즈를 등록합니다.")
    public ResponseEntity<ImageQuizResponseDto> createImageQuiz(
            @RequestParam("category") String category,
            @RequestParam("question") String question,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("answer") String answer,
            @RequestParam("choices") String choicesJson
    ) throws Exception {
        List<String> choices = objectMapper.readValue(choicesJson, new TypeReference<>() {});
        String imageUrl = fileStorageService.saveFile(imageFile);
        ImageQuizContent entity = imageQuizContentService.create(category, question, imageUrl, answer, choices);
        return ResponseEntity.ok(ImageQuizResponseDto.fromEntity(entity));
    }

    @PostMapping(value = "/{id}", consumes = "multipart/form-data")
    @Operation(summary = "이미지 퀴즈 수정", description = "기존 이미지 퀴즈의 내용 및 이미지를 수정합니다. imageFile은 선택 사항입니다.")
    public ResponseEntity<ImageQuizResponseDto> updateImageQuiz(
            @PathVariable Long id,
            @RequestParam("category") String category,
            @RequestParam("question") String question,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam("answer") String answer,
            @RequestParam("choices") String choicesJson
    ) throws Exception {
        List<String> choices = objectMapper.readValue(choicesJson, new TypeReference<>() {});
        String newImageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            newImageUrl = fileStorageService.saveFile(imageFile);
        }
        ImageQuizContent entity = imageQuizContentService.update(id, category, question, newImageUrl, answer, choices);
        return ResponseEntity.ok(ImageQuizResponseDto.fromEntity(entity));
    }

    @GetMapping("/{id}")
    @Operation(summary = "이미지 퀴즈 상세 조회", description = "이미지 퀴즈의 상세 정보를 조회합니다.")
    public ResponseEntity<ImageQuizResponseDto> getImageQuiz(@PathVariable Long id) {
        ImageQuizContent entity = imageQuizContentService.findById(id);
        return ResponseEntity.ok(ImageQuizResponseDto.fromEntity(entity));
    }
}
