package JesusDeciples.RankingQuiz.api.service.quizDataCenter.state;

import JesusDeciples.RankingQuiz.api.service.quizDataCenter.QuizDataCenter;

public class INIT_SCORE implements DataCenterState {
    @Override
    public void handle(QuizDataCenter quizDataCenter) {
        quizDataCenter.score();
        quizDataCenter.setPresentState(new COMPLETE_SCORE());
    }
}
