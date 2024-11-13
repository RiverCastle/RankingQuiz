package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.DataCenterState;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.voca.VocaQuizDataCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class VocaQuizDataCenterMediator {
    private final VocaQuizDataCenter quizDataCenter;

    public DataCenterState getQuizDataCenterState() {
        return quizDataCenter.getPresentState();
    }

    public void updateDataCenterStateAndAction(DataCenterState dataCenterState) {
        quizDataCenter.setPresentState(dataCenterState);
        quizDataCenter.handle();
    }

    public Map<String, QuizResultDto> getQuizResults() {
        return quizDataCenter.getResults();
    }

    public QuizDto getPresentQuizDto() {
        return quizDataCenter.getPresentQuizDto();
    }

    public String getQuizWinnerName() {
        return quizDataCenter.getWinnerName();
    }
}