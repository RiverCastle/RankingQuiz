package JesusDeciples.RankingQuiz.api.service.quizDataCenter.state;

import JesusDeciples.RankingQuiz.api.service.quizDataCenter.VocaQuizDataCenter;

public class INIT_SCORE implements DataCenterState {
    @Override
    public void handle(VocaQuizDataCenter vocaQuizDataCenter) {
        vocaQuizDataCenter.score();
        vocaQuizDataCenter.setPresentState(new COMPLETE_SCORE());
    }
}
