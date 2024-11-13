package JesusDeciples.RankingQuiz.api.service.quizDataCenter;

import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.DataCenterState;

import java.time.LocalDateTime;
import java.util.Map;

public interface QuizDataCenter {
    LocalDateTime getPresentQuizFinishedAt();
    void setPresentState(DataCenterState state);
    DataCenterState getPresentState();
    void clearDataCenter();
    void score();
    void initiateQuiz();
    void setNewQuizExcept();
    void handle();
    Map<String, QuizResultDto> getResults();
    QuizDto getPresentQuizDto();
    String getWinnerName();
}
