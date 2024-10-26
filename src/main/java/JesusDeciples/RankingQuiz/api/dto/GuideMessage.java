package JesusDeciples.RankingQuiz.api.dto;

import lombok.Data;

@Data
public class GuideMessage {
    private String message;
    private boolean display;

    public GuideMessage(String message) {
        this.message = message;
    }
}
