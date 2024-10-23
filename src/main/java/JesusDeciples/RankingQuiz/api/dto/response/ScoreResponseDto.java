package JesusDeciples.RankingQuiz.api.dto.response;

import lombok.Data;

@Data
public class ScoreResponseDto {
    private boolean isCorrect;

    public ScoreResponseDto(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
