package JesusDeciples.RankingQuiz.api.service.quizDataCenter.state;

import JesusDeciples.RankingQuiz.api.service.quizDataCenter.VocaQuizDataCenter;

public class INIT_QUIZ implements DataCenterState{
    @Override
    public void handle(VocaQuizDataCenter vocaQuizDataCenter) {
        vocaQuizDataCenter.setPresentState(this);
        vocaQuizDataCenter.initiateQuiz();
        /*
        상태 업데이트 없음
        생성된 퀴즈로 웹소켓에서 메시지를 전송 후 상태 업데이트
         */
    }
}
