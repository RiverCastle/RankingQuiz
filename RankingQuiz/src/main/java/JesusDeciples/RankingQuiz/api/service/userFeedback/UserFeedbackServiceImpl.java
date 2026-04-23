package JesusDeciples.RankingQuiz.api.service.userFeedback;

import JesusDeciples.RankingQuiz.api.dto.FeedbackCategory;
import JesusDeciples.RankingQuiz.api.dto.IssuePostBody;
import JesusDeciples.RankingQuiz.api.dto.UserFeedbackDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFeedbackServiceImpl implements UserFeedbackService {
    @Value("${ISSUE_POST_TOKEN}")
    private String githubToken;
    @Value("${RANKING_QUIZ_BACKEND_REPO_ISSUE}")
    private String backend_repo;
    @Value("${RANKING_QUIZ_FRONTEND_REPO_ISSUE}")
    private String frontend_repo;

    @Override
    public void postFeedback(UserFeedbackDto dto) throws JsonProcessingException {
        FeedbackCategory feedbackCategory = dto.getCategory();
        String feedbackContent = dto.getContent();
        String requestBody = produceRequestBody(feedbackCategory, feedbackContent);
        System.out.println(requestBody);
        switch (feedbackCategory) {
            case NotSelected, NewFeatureProposal -> {
                postBackendIssueOnGithub(requestBody);
                postFrontendIssueOnGithub(requestBody);
            }
            case BackendIssue -> postBackendIssueOnGithub(requestBody);
            case FrontendIssue -> postFrontendIssueOnGithub(requestBody);
        }
    }

    @Override
    public void postNewFeature(String requestBody) {
        postBackendIssueOnGithub(requestBody);
        postFrontendIssueOnGithub(requestBody);
    }

    @Override
    public void postBackendIssueOnGithub(String requestBody) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", githubToken); // GitHub Token
        headers.add("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.postForEntity(backend_repo, entity, String.class);

        // TODO 개발자에게 알림
    }


    @Override
    public void postFrontendIssueOnGithub(String requestBody) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", githubToken);
        headers.add("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.postForEntity(frontend_repo, entity, String.class);

        // TODO 개발자에게 알림
    }



    private String produceRequestBody(FeedbackCategory feedbackCategory, String feedbackContent) throws JsonProcessingException {
        // 제목과 라벨 결정
        String title;
        String label;
        switch (feedbackCategory) {
            case NewFeatureProposal -> title = "Ranking Quiz 사용자로부터 제안받은 기능";
            case FrontendIssue -> title = "Ranking Quiz 사용자로부터 보고받은 프론트엔드 문제";
            case BackendIssue -> title = "Ranking Quiz 사용자로부터 보고받은 백엔드 문제";
            case NotSelected -> title = "Ranking Quiz 사용자로부터 보고받은 미분류 문제";
            default -> title = "기본 제목";
        }
        switch (feedbackCategory) {
            case NewFeatureProposal -> label = "suggestion";
            case FrontendIssue, BackendIssue, NotSelected -> label = "bug";
            default -> label = "";
        }

        IssuePostBody requestBody = new IssuePostBody(title,
                feedbackContent, List.of("RiverCastle"), List.of(label));

        // ObjectMapper를 사용하여 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(requestBody);
    }
}
