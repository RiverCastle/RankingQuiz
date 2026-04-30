package JesusDeciples.RankingQuiz.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class ServiceFeedbackDto {
    private Integer satisfactionScore;
    private List<String> serviceTags;
    private String generalComment;
    private String category;
    private String deviceInfo;
}
