package JesusDeciples.RankingQuiz.api.service.quizDataCenter.state;

import JesusDeciples.RankingQuiz.api.service.quizDataCenter.QuizDataCenter;

public class WAITING implements DataCenterState {
    @Override
    public void handle(QuizDataCenter quizDataCenter) {
        if (!(quizDataCenter.getPresentState() instanceof WAITING)) {
            quizDataCenter.setPresentState(this);
            quizDataCenter.clearDataCenter();
        }
        /*
        상태 업데이트 없음
        새 세션이 추가되면 INIT_QUIZ로 상태 업데이트
         */
    }
}
