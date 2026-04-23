package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.UserFeedbackDto;
import JesusDeciples.RankingQuiz.api.service.userFeedback.UserFeedbackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-feedback")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "UserFeedback API", description = "서비스 이용 후기 API 입니다.")
public class UserFeedbackController {
    private final UserFeedbackService userFeedbackService;

    @PostMapping
    @Operation(summary = "사용자 피드백 제출",
            description = "사용자의 서비스 이용 후기를 GitHub Issue API를 사용해 Repository Issues에 등록합니다.")
    public void postUserFeedback(@RequestBody UserFeedbackDto[] userFeedbackDtos) throws JsonProcessingException {
        for (UserFeedbackDto dto : userFeedbackDtos)
            userFeedbackService.postFeedback(dto);
    }
}
