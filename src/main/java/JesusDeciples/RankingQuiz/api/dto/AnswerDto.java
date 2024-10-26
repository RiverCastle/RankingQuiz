package JesusDeciples.RankingQuiz.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerDto {
    private Long quizId;
    private String userAnswer;
    private LocalDateTime writtenAt;
    private String userName;
}
