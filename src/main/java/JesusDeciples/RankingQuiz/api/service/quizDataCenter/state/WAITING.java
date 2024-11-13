package JesusDeciples.RankingQuiz.api.service.quizDataCenter.state;

import JesusDeciples.RankingQuiz.api.service.quizDataCenter.VocaQuizDataCenter;

public class WAITING implements DataCenterState {
    @Override
    public void handle(VocaQuizDataCenter vocaQuizDataCenter) {
        if (!(vocaQuizDataCenter.getPresentState() instanceof WAITING)) {
            vocaQuizDataCenter.setPresentState(this);
            vocaQuizDataCenter.clearDataCenter();
        }
        /*
        상태 업데이트 없음
        새 세션이 추가되면 INIT_QUIZ로 상태 업데이트
         */
    }
}
