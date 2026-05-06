package JesusDeciples.RankingQuiz.api.dto.request;

import JesusDeciples.RankingQuiz.api.enums.ReportStatus;
import lombok.Data;

@Data
public class ReportStatusUpdateDto {
    private ReportStatus status;
}
