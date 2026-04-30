package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.ServiceFeedbackDto;
import JesusDeciples.RankingQuiz.api.service.serviceFeedback.ServiceFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-feedback")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "ServiceFeedback API", description = "서비스 수준 만족도 피드백 API 입니다.")
public class ServiceFeedbackController {

    private final ServiceFeedbackService serviceFeedbackService;

    @PostMapping
    @Operation(summary = "서비스 만족도 피드백 제출",
            description = "사용자의 서비스 전반 만족도(점수, 태그, 코멘트)를 DB에 저장합니다.")
    public ResponseEntity<Void> postServiceFeedback(@RequestBody ServiceFeedbackDto dto) {
        serviceFeedbackService.save(dto);
        return ResponseEntity.ok().build();
    }
}
