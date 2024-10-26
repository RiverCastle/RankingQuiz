package JesusDeciples.RankingQuiz.api.webSocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class QuizSystemStateChangeDetector {

    public TextMessage executeLogic(QuizSystemState nextState) {
        TextMessage message = null;
        switch (nextState) {
            case ON_QUIZ_SETTING: // 퀴즈를 생성 중
                startMakingNewQuiz();
                break;
            case COMPLETED_QUIZ_SETTING: // 퀴즈 생성 완료
                sendNewQuizDto();
                break;
            case ON_QUIZ: // 퀴즈 시작 - 진행 중
                break;
            case COMPLETED_QUIZ_GETTING_ANSWERED: // 퀴즈 종료 - 답안 수거
                startCollectingAnswers();
                break;
            case ON_SCORING: // 채점 시작
                startScoring();
                break;
            case COMPLETED_SCORING: // 채점 완료
                sendResults();
                break;
            case WAITING: // 대기 상태 퀴즈 진행 X
                sendWaitingMessage();
                break;

            default:
                // 기본 로직 또는 예외 처리
                break;
        }
        return message;
    }

    private void sendNewQuizDto() {
    }

    private void sendWaitingMessage() {
    }

    private void sendResults() {
    }

    private void startScoring() {
//        return을 Set<WebsocketSession, QuizResult>로 하면 좋을듯
//        그러면 Handle가 전체 세션에 대한 퀴즈 결과를 가지고 있고,
//        현 재접속중인 세션에 대해서 각 세션의 퀴즈 결과를 보내준다.
    }

    private void startCollectingAnswers() {
    }

    private void sendNewQuizDtoToSessions() {
    }

    private void startMakingNewQuiz() {
    }
}
