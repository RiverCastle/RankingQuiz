package JesusDeciples.RankingQuiz.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BulkImportResultDto {
    private final boolean success;
    private final int savedCount;
    private final List<BulkImportRowErrorDto> errors;
}
