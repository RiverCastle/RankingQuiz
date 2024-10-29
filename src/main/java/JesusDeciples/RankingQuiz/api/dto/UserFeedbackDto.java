package JesusDeciples.RankingQuiz.api.dto;

import lombok.Data;

@Data
public class UserFeedbackDto {
    private String content;
    private FeedbackCategory category;
}
