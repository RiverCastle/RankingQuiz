package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.VocaQuizDataCenter;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.DataCenterState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class VocaQuizDataCenterMediator {
    private final VocaQuizDataCenter vocaQuizDataCenter;

    public DataCenterState getQuizDataCenterState() {
        return vocaQuizDataCenter.getPresentState();
    }

    public void updateDataCenterStateAndAction(DataCenterState dataCenterState) {
        vocaQuizDataCenter.setPresentState(dataCenterState);
        vocaQuizDataCenter.handle();
    }

    public Map<String, QuizResultDto> getQuizResults() {
        return vocaQuizDataCenter.getResults();
    }

    public QuizDto getPresentQuizDto() {
        return vocaQuizDataCenter.getPresentQuizDto();
    }

    public String getQuizWinnerName() {
        return vocaQuizDataCenter.getWinnerName();
    }
}
