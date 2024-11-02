package JesusDeciples.RankingQuiz.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerDto {
    private Long quizId;
    private String userAnswer;
    private LocalDateTime writtenAt;
    private String userName;
    private Long memberId; // 비회원 사용자의 경우 null 값을 가지도록 long x Long o
}
