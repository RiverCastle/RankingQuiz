package JesusDeciples.RankingQuiz.api.service.quizDataCenter.state;

import JesusDeciples.RankingQuiz.api.service.quizDataCenter.QuizDataCenter;

public class INIT_NEXT_QUIZ implements DataCenterState {
    @Override
    public void handle(QuizDataCenter quizDataCenter) {
        quizDataCenter.setPresentState(this);
        quizDataCenter.setNewQuizExcept();
        /*
        상태 업데이트 없음
        바뀐 퀴즈로 웹소켓에서 메시지를 전송 후 상태 업데이트
         */
    }
}
