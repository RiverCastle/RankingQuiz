package JesusDeciples.RankingQuiz.api.service.quizDataCenter.state;

import JesusDeciples.RankingQuiz.api.webSocket.QuizDataCenter;

public interface DataCenterState {
    void handle(QuizDataCenter quizDataCenter);
}
