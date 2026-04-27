package JesusDeciples.RankingQuiz.api.dto;

import lombok.Data;

@Data
public class GuideMessage {
    private String message;
    private boolean display = false;

    public GuideMessage(String message) {
        this.message = message;
    }
}
