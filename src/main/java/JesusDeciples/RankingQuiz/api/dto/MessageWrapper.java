package JesusDeciples.RankingQuiz.api.dto;

import lombok.Data;

@Data
public class MessageWrapper {
    private String dataType;
    private Object messageObject;
}
