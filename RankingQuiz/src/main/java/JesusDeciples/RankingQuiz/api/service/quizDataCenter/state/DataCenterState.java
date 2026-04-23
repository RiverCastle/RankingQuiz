package JesusDeciples.RankingQuiz.api.service.quizDataCenter.state;

import JesusDeciples.RankingQuiz.api.service.quizDataCenter.QuizDataCenter;

public interface DataCenterState {
    void handle(QuizDataCenter quizDataCenter);
}
