package JesusDeciples.RankingQuiz.api.service.quizDataCenter;

import JesusDeciples.RankingQuiz.api.webSocket.QuizDataCenter;

public interface DataCenterState {
    void handle(QuizDataCenter quizDataCenter);
}
