package JesusDeciples.RankingQuiz.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizDto {
    private Long quizId;
    private String statement;
    private QuizContentDto quizContentDto;
    private LocalDateTime finishedAt;

}
