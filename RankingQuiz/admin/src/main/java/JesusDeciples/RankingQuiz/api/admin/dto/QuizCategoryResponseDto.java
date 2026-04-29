package JesusDeciples.RankingQuiz.api.admin.dto;

import JesusDeciples.RankingQuiz.api.entity.QuizCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizCategoryResponseDto {
    private String code;
    private String displayName;
    private boolean enabled;
    private boolean allowMultipleWinners;

    public static QuizCategoryResponseDto from(QuizCategory entity) {
        return new QuizCategoryResponseDto(
                entity.getCode(),
                entity.getDisplayName(),
                entity.isEnabled(),
                entity.isAllowMultipleWinners()
        );
    }
}
