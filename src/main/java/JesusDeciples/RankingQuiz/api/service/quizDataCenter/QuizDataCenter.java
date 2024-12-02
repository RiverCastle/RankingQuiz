package JesusDeciples.RankingQuiz.api.service.quizDataCenter;

import JesusDeciples.RankingQuiz.api.dto.AnswerDto;
import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.DataCenterState;

import java.time.LocalDateTime;
import java.util.Map;

public abstract class QuizDataCenter {
    public abstract LocalDateTime getPresentQuizFinishedAt();
    public abstract void setPresentState(DataCenterState state);
    public abstract DataCenterState getPresentState();
    public abstract void clearDataCenter();
    public abstract void score();
    public abstract void initiateQuiz();
    public abstract void setNewQuizExcept();
    public abstract void handle();
    public abstract Map<String, QuizResultDto> getResults();
    public abstract QuizDto getPresentQuizDto();
    public abstract String getWinnerName();
    public abstract void loadAnswerFromUser(AnswerDto answerDto);
}
