package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.AnswerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerDtoMessageHandler {
    private final ObjectMapper objectMapper;
    private final QuizDataCenter quizDataCenter;

    public void handleAnswerDtoMessageObject(String sessionId, Long memberId, Object objectInMessage) {
        Long presentQuizId = quizDataCenter.getPresentQuizDto().getQuizId();
        AnswerDto answerDto = objectMapper.convertValue(objectInMessage, AnswerDto.class);
        Long quizIdInAnswerDto = answerDto.getQuizId();
        if (!presentQuizId.equals(quizIdInAnswerDto)) return; // 현재 퀴즈와 무관한 답안

        // 엑세스 토큰을 가진 세션의 경우 memberId 값이 있음
        answerDto.setMemberId(memberId);
        quizDataCenter.loadAnswerFromUser(sessionId, answerDto);
    }


}
