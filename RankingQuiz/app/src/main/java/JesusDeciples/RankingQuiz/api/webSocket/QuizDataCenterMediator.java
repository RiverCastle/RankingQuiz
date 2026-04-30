package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.AnswerDto;
import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.QuizDataCenter;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.DataCenterState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QuizDataCenterMediator {
    private final Map<QuizCategory, QuizDataCenter> quizDataCenters;
    private final ObjectMapper objectMapper;

    public QuizDataCenterMediator(List<QuizDataCenter> dataCenters, ObjectMapper objectMapper) {
        this.quizDataCenters = dataCenters.stream()
                .collect(Collectors.toMap(QuizDataCenter::getCategory, dc -> dc));
        this.objectMapper = objectMapper;
    }

    public DataCenterState getQuizDataCenterState(QuizCategory category) {
        return quizDataCenters.get(category).getPresentState();
    }

    public void updateDataCenterStateAndAction(QuizCategory category, DataCenterState dataCenterState) {
        QuizDataCenter quizDataCenter = quizDataCenters.get(category);
        quizDataCenter.setPresentState(dataCenterState);
        quizDataCenter.handle();
    }

    public Map<String, QuizResultDto> getQuizResults(QuizCategory category) {
        return quizDataCenters.get(category).getResults();
    }

    public QuizDto getPresentQuizDto(QuizCategory category) {
        return quizDataCenters.get(category).getPresentQuizDto();
    }

    public String getQuizWinnerName(QuizCategory category) {
        return quizDataCenters.get(category).getWinnerName();
    }

    public void sendAnswerToDataCenter(QuizCategory category, String sessionId, Long memberId, Object objectInMessage) {
        QuizDataCenter quizDataCenter = quizDataCenters.get(category);
        Long presentQuizId = quizDataCenter.getPresentQuizDto().getQuizId();
        AnswerDto answerDto = objectMapper.convertValue(objectInMessage, AnswerDto.class);
        Long quizIdInAnswerDto = answerDto.getQuizId();
        answerDto.setWrittenAt(LocalDateTime.now());
        if (!presentQuizId.equals(quizIdInAnswerDto)) return;

        answerDto.setMemberId(memberId);
        answerDto.setSessionId(sessionId);
        quizDataCenter.loadAnswerFromUser(answerDto);
    }
}
