package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static JesusDeciples.RankingQuiz.api.webSocket.QuizDataCenterState.*;
import static JesusDeciples.RankingQuiz.api.webSocket.QuizDataCenterState.COMPLETED_SCORING;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuizDataCenterMediator {
    private final QuizDataCenter quizDataCenter;

    public QuizDataCenterState getQuizDataCenterState() {
        return quizDataCenter.getState();
    }
    public void updateQuizDataCenterState(QuizDataCenterState nextState) {
        QuizDataCenterState presentState = getQuizDataCenterState();
        if (presentState == nextState) return;
        else if (presentState == ON_QUIZ & nextState == COMPLETED_QUIZ_GETTING_ANSWERED) {
            quizDataCenter.setState(nextState);
        } else if (presentState == COMPLETED_QUIZ_GETTING_ANSWERED & nextState == ON_SCORING) {
            quizDataCenter.setState(nextState);
            quizDataCenter.score();
            updateQuizDataCenterState(COMPLETED_SCORING);
        } else if (presentState == ON_SCORING & nextState == COMPLETED_SCORING) {
            quizDataCenter.setState(nextState);
        } else if (presentState == COMPLETED_SCORING & nextState == ON_QUIZ_SETTING) {
            quizDataCenter.setState(nextState);
            quizDataCenter.setNewQuizExcept();
        } else if (presentState == WAITING & nextState == ON_QUIZ_SETTING) {
            quizDataCenter.setState(nextState);
            quizDataCenter.initiateQuiz();
        } else if (presentState == ON_QUIZ_SETTING & nextState == ON_QUIZ) {
            quizDataCenter.setState(nextState);
        } else if (nextState == WAITING) {
            quizDataCenter.clearDataCenter();
        }
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
