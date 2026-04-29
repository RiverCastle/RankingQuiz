package JesusDeciples.RankingQuiz.api.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class QuizCategoryCreateDto {
    @NotBlank
    private String code;
    @NotBlank
    private String displayName;
    private boolean allowMultipleWinners;
}
