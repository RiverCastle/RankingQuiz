package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.admin.service.QuizCategoryService;
import JesusDeciples.RankingQuiz.api.dto.AnswerDto;
import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.entity.QuizCategory;
import JesusDeciples.RankingQuiz.api.facade.QuizQuizContentFacade;
import JesusDeciples.RankingQuiz.api.facade.QuizScoreFacade;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.GenericQuizDataCenter;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.DataCenterState;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class QuizDataCenterMediator {

    private final QuizCategoryService quizCategoryService;
    private final QuizScoreFacade quizScoreFacade;
    private final QuizQuizContentFacade quizQuizContentFacade;
    private final ObjectMapper objectMapper;

    private final ConcurrentHashMap<String, GenericQuizDataCenter> dataCenters = new ConcurrentHashMap<>();

    private GenericQuizDataCenter getOrCreate(String categoryCode) {
        return dataCenters.computeIfAbsent(categoryCode, code -> {
            QuizCategory cat = quizCategoryService.getCategory(code);
            return new GenericQuizDataCenter(code, cat.isAllowMultipleWinners(),
                    quizScoreFacade, quizQuizContentFacade);
        });
    }

    public DataCenterState getQuizDataCenterState(String categoryCode) {
        return getOrCreate(categoryCode).getPresentState();
    }

    public void updateDataCenterStateAndAction(String categoryCode, DataCenterState dataCenterState) {
        GenericQuizDataCenter dc = getOrCreate(categoryCode);
        dc.setPresentState(dataCenterState);
        dc.handle();
    }

    public Map<String, QuizResultDto> getQuizResults(String categoryCode) {
        return getOrCreate(categoryCode).getResults();
    }

    public QuizDto getPresentQuizDto(String categoryCode) {
        return getOrCreate(categoryCode).getPresentQuizDto();
    }

    public String getQuizWinnerName(String categoryCode) {
        return getOrCreate(categoryCode).getWinnerName();
    }

    public void sendAnswerToDataCenter(String categoryCode, String sessionId, Long memberId, Object objectInMessage) {
        GenericQuizDataCenter dc = getOrCreate(categoryCode);
        Long presentQuizId = dc.getPresentQuizDto().getQuizId();
        AnswerDto answerDto = objectMapper.convertValue(objectInMessage, AnswerDto.class);
        Long quizIdInAnswerDto = answerDto.getQuizId();
        answerDto.setWrittenAt(LocalDateTime.now());
        if (!presentQuizId.equals(quizIdInAnswerDto)) return;
        answerDto.setMemberId(memberId);
        answerDto.setSessionId(sessionId);
        dc.loadAnswerFromUser(answerDto);
    }
}
