package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static JesusDeciples.RankingQuiz.api.webSocket.QuizSystemState.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketQuizHandler {
    private final Long waitingTime = 3L; // 퀴즈 수거 대기 시간
    private final CustomTextMessageFactory textMessageFactory;
    private QuizSystemState presentState = WAITING;
    private final QuizDataCenter quizDataCenter;
    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    private final String guideMessage_NoAnswer = "퀴즈에 참가하지 않으셨어요. 다음 퀴즈에는 꼭 참가해보세요!";

    @Scheduled(fixedDelay = 1000)
    private void checkPresentState() throws IOException {

        switch (presentState) {
            case ON_QUIZ -> {
                log.info("퀴즈 진행중 채점 곧 시작함");
                log.info("CURRENT QUIZ ID = " + quizDataCenter.getPresentQuiz().getId());
                LocalDateTime quizFinishedAt = quizDataCenter.getPresentQuizFinishedAt();
                LocalDateTime now = LocalDateTime.now();
                if (now.isAfter(quizFinishedAt)) {
                    log.info("퀴즈 종료 - 답안 수집 시작");
                    updateQuizSystemState(COMPLETED_QUIZ_GETTING_ANSWERED);
                }
                break;
            }
            case COMPLETED_QUIZ_GETTING_ANSWERED -> {
                log.info("답안 수집 진행 중");
                LocalDateTime collectingAnswersFinishedAt = quizDataCenter.getPresentQuizFinishedAt().plusSeconds(waitingTime);
                LocalDateTime now = LocalDateTime.now();
                if (now.isAfter(collectingAnswersFinishedAt)) {
                    log.info("정답 수거 종료 채점 시작");
                    updateQuizSystemState(ON_SCORING);
                }
                break;
            }
        }
    }

    public void updateQuizSystemState(QuizSystemState nextState) throws IOException {
        if (this.presentState != nextState) {
            if (presentState == ON_QUIZ & nextState == COMPLETED_QUIZ_GETTING_ANSWERED) {
                // 퀴즈 중 -> 퀴즈 종료, 답안 수거로 상태 변경
                log.info("현재 상태 = " + presentState +" 다음 상태 = " + nextState);
                this.presentState = nextState;
            }
            else if (presentState == COMPLETED_QUIZ_GETTING_ANSWERED & nextState == ON_SCORING) {
                // 퀴즈 종료, 답안 수거 -> 채점 시작
                log.info("현재 상태 = " + presentState +" 다음 상태 = " + nextState);
                this.presentState = nextState;
                quizDataCenter.score();
                updateQuizSystemState(COMPLETED_SCORING);
            }
            else if (presentState == ON_SCORING & nextState == COMPLETED_SCORING) {
                // 채점 시작 -> 채점 종료
                log.info("현재 상태 = " + presentState +" 다음 상태 = " + nextState);
                this.presentState = nextState;
                Set<String> sessionIds = sessions.keySet();
                Map<String, QuizResultDto> results = quizDataCenter.getResults();
                Set<String> sessionIdsOfParticipants = results.keySet();
                for (String sessionId : sessionIds) {// QuizResult -> TextMessage 변환
                    if (sessionIdsOfParticipants.contains(sessionId)) {// 활성 참가자 중 이전 퀴즈 참가자에게 퀴즈 결과 전송
                        TextMessage textMessage = new TextMessage(
                                textMessageFactory.produceTextMessage(results.get(sessionId)));
                        sessions.get(sessionId).sendMessage(textMessage);
                    } else {
                        String message = textMessageFactory.produceTextMessage(guideMessage_NoAnswer);
                        sessions.get(sessionId).sendMessage(new TextMessage(message));
                    }
                }
                updateQuizSystemState(ON_QUIZ_SETTING);
            }
            else if (presentState == COMPLETED_SCORING & nextState == ON_QUIZ_SETTING) {
                // 채점 종료 -> 새 퀴즈 생성
                // 전체 세션에 새 퀴즈 전송
                log.info("현재 상태 = " + presentState +" 다음 상태 = " + nextState);
                this.presentState = nextState;
                quizDataCenter.setNewQuizExcept(); // 새 퀴즈 생성 이전 QuizContent 제외
                String newQuizTextMessage = // 새 퀴즈 메시지로 변환
                        textMessageFactory.produceTextMessage(quizDataCenter.getPresentQuizDto());

                Set<String> sessionIds = sessions.keySet();
                for (String sessionId : sessionIds) // 전체 활성 참가자에게 전송
                    sessions.get(sessionId).sendMessage(new TextMessage(newQuizTextMessage));
                updateQuizSystemState(ON_QUIZ);
            }
            else if (presentState == WAITING & nextState == ON_QUIZ_SETTING) {
                log.info("현재 상태 = " + presentState +" 다음 상태 = " + nextState);
                // 대기 상태 -> 퀴즈 생성
                // 전체 세션에 새 퀴즈 메시지 전송
                log.info("퀴즈 최초 시작");
                this.presentState = nextState;
                quizDataCenter.initiateQuiz(); // 퀴즈를 처음 시작 QuizContent 중 예외없이 임의로 출제
                String newQuizTextMessage = // 새 퀴즈 메시지로 변환
                        textMessageFactory.produceTextMessage(quizDataCenter.getPresentQuizDto());

                Set<String> sessionIds = sessions.keySet(); // 모든 세션에게 퀴즈 전송
                for (String sessionId : sessionIds)
                    sessions.get(sessionId).sendMessage(new TextMessage(newQuizTextMessage));
                updateQuizSystemState(ON_QUIZ);
            }
            else if (presentState == ON_QUIZ_SETTING & nextState == ON_QUIZ) {
                // 퀴즈 생성 완료 -> 퀴즈 시작
                log.info("현재 상태 = " + presentState +" 다음 상태 = " + nextState);
                this.presentState = ON_QUIZ;
            }
        }
    }
}
