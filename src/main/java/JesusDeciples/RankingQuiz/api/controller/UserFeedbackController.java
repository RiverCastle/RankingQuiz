package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.UserFeedbackDto;
import JesusDeciples.RankingQuiz.api.service.userFeedback.UserFeedbackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-feedback")
public class UserFeedbackController {
    private final UserFeedbackService userFeedbackService;

    @PostMapping
    @CrossOrigin(origins = "https://rankingquiz.rivercastleworks.site")
    public void postUserFeedback(@RequestBody UserFeedbackDto[] userFeedbackDtos) throws JsonProcessingException {
        for (UserFeedbackDto dto : userFeedbackDtos)
            userFeedbackService.postFeedback(dto);
    }
}
