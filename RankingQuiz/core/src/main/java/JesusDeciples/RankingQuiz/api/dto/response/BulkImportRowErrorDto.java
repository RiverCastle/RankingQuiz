package JesusDeciples.RankingQuiz.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BulkImportRowErrorDto {
    private final int rowNumber;
    private final String message;
}
