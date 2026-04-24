package JesusDeciples.RankingQuiz.api.service.userFeedback;

import JesusDeciples.RankingQuiz.api.dto.UserFeedbackDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface UserFeedbackService {
    void postFeedback(UserFeedbackDto dto) throws JsonProcessingException;
    void postNewFeature(String feedbackContent);
    void postBackendIssueOnGithub(String feedbackContent);
    void postFrontendIssueOnGithub(String feedbackContent);
}
