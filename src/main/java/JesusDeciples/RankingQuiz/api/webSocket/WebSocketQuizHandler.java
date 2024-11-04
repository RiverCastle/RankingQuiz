package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.AnswerDto;
import JesusDeciples.RankingQuiz.api.dto.GuideMessage;
import JesusDeciples.RankingQuiz.api.dto.GuideMessageBundle;
import JesusDeciples.RankingQuiz.api.dto.MessageWrapper;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.jwt.JWTProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static JesusDeciples.RankingQuiz.api.webSocket.QuizSystemState.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketQuizHandler implements WebSocketHandler {
    private final Long waitingTime = 3L; // 퀴즈 수거 대기 시간
    private final CustomTextMessageFactory textMessageFactory;
    private QuizSystemState presentState = WAITING;
    private final QuizDataCenter quizDataCenter;
    private final Map<String, WebSocketSession> sessions = new HashMap<>();
    private final GuideMessageBundle guideMessageBundle;
    private final ObjectMapper objectMapper;
    private final JWTProducer jwtProducer;

    @Scheduled(fixedDelay = 1000)
    private void checkPresentState() throws IOException, InterruptedException {
        switch (presentState) {
            case ON_QUIZ -> {
                LocalDateTime quizFinishedAt = quizDataCenter.getPresentQuizFinishedAt().minusSeconds(2L);
                LocalDateTime now = LocalDateTime.now();

                if (now.isAfter(quizFinishedAt)) {
                    updateQuizSystemState(COMPLETED_QUIZ_GETTING_ANSWERED);
                }
                break;
            }
            case COMPLETED_QUIZ_GETTING_ANSWERED -> {
                LocalDateTime collectingAnswersFinishedAt = quizDataCenter.getPresentQuizFinishedAt().plusSeconds(waitingTime);
                LocalDateTime now = LocalDateTime.now();
                if (now.isAfter(collectingAnswersFinishedAt)) {
                    updateQuizSystemState(ON_SCORING);
                }
                break;
            }
        }
    }

    public void updateQuizSystemState(QuizSystemState nextState) throws IOException, InterruptedException {
        if (this.presentState != nextState) {
            if (presentState == ON_QUIZ & nextState == COMPLETED_QUIZ_GETTING_ANSWERED) {
                // 퀴즈 중 -> 퀴즈 종료, 답안 수거로 상태 변경
                this.presentState = nextState;
            } else if (presentState == COMPLETED_QUIZ_GETTING_ANSWERED & nextState == ON_SCORING) {
                // 퀴즈 종료, 답안 수거 -> 채점 시작
                this.presentState = nextState;
                quizDataCenter.score();
                updateQuizSystemState(COMPLETED_SCORING);
            } else if (presentState == ON_SCORING & nextState == COMPLETED_SCORING) {
                // 채점 시작 -> 채점 종료
                this.presentState = nextState;

                Set<String> sessionIds = sessions.keySet();
                Map<String, QuizResultDto> results = quizDataCenter.getResults();
                Set<String> sessionIdsOfParticipants = results.keySet();

                String winner = (quizDataCenter.getWinnerName() == null) ? "없습니다." : quizDataCenter.getWinnerName() + "님입니다.";
                GuideMessage winner_notification = new GuideMessage("이번 퀴즈의 우승자는 " + winner);
                winner_notification.setDisplay(true);

                guideMessageBundle.setWinner_notification(winner_notification);
                TextMessage winnerAnouncementTextMessage =
                        textMessageFactory.produceTextMessage(guideMessageBundle.getWinner_notification());

                for (String sessionId : sessionIds) {// QuizResult -> TextMessage 변환
                    if (sessionIdsOfParticipants.contains(sessionId)) {// 활성 참가자 중 이전 퀴즈 참가자에게 퀴즈 결과 전송
                        TextMessage textMessage =
                                textMessageFactory.produceTextMessage(results.get(sessionId));
                        sessions.get(sessionId).sendMessage(textMessage);
                        sessions.get(sessionId).sendMessage(winnerAnouncementTextMessage);
                    } else {
                        TextMessage message =
                                textMessageFactory.produceTextMessage(guideMessageBundle.getNotParticipatedMessage());
                        sessions.get(sessionId).sendMessage(message);
                    }
                }
                Thread.sleep(5000);
                updateQuizSystemState(ON_QUIZ_SETTING);
            } else if (presentState == COMPLETED_SCORING & nextState == ON_QUIZ_SETTING) {
                // 채점 종료 -> 새 퀴즈 생성
                // 전체 세션에 새 퀴즈 전송
                this.presentState = nextState;
                quizDataCenter.setNewQuizExcept(); // 새 퀴즈 생성 이전 QuizContent 제외
                TextMessage newQuizTextMessage = // 새 퀴즈 메시지로 변환
                        textMessageFactory.produceTextMessage(quizDataCenter.getPresentQuizDto());
                Set<String> sessionIds = sessions.keySet();
                for (String sessionId : sessionIds) // 전체 활성 참가자에게 전송
                    sessions.get(sessionId).sendMessage(newQuizTextMessage);
                updateQuizSystemState(ON_QUIZ);
            } else if (presentState == WAITING & nextState == ON_QUIZ_SETTING) {
                // 대기 상태 -> 퀴즈 생성
                // 전체 세션에 새 퀴즈 메시지 전송
                this.presentState = nextState;
                quizDataCenter.initiateQuiz(); // 퀴즈를 처음 시작 QuizContent 중 예외없이 임의로 출제
                TextMessage newQuizTextMessage = // 새 퀴즈 메시지로 변환
                        textMessageFactory.produceTextMessage(quizDataCenter.getPresentQuizDto());
                Set<String> sessionIds = sessions.keySet(); // 모든 세션에게 퀴즈 전송
                for (String sessionId : sessionIds)
                    sessions.get(sessionId).sendMessage(newQuizTextMessage);
                updateQuizSystemState(ON_QUIZ);
            } else if (presentState == ON_QUIZ_SETTING & nextState == ON_QUIZ) {
                // 퀴즈 생성 완료 -> 퀴즈 시작
                this.presentState = ON_QUIZ;
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException, InterruptedException {
        sessions.put(session.getId(), session);
        // 대기상태 중 새 세션
        if (presentState == WAITING) {
            updateQuizSystemState(ON_QUIZ_SETTING);
        }
        session.sendMessage(textMessageFactory.produceTextMessage(guideMessageBundle.getPrepareMessage()));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof BinaryMessage) return;

        MessageWrapper messageWrapperFromClient = objectMapper.readValue(((TextMessage) message).getPayload(), MessageWrapper.class);
        String dataType = messageWrapperFromClient.getDataType();
        switch (dataType) {
            case "AccessToken" -> {
                String accessToken = messageWrapperFromClient.getObject().toString();
                Long memberId = Long.parseLong(jwtProducer.extractSubject(accessToken));
                session.getAttributes().put("memberId", memberId);
            }

            case "AnswerDto" -> {
                if (presentState != COMPLETED_QUIZ_GETTING_ANSWERED) return;

                Object objectInMessage = messageWrapperFromClient.getObject();
                AnswerDto answerDto = objectMapper.convertValue(objectInMessage, AnswerDto.class);
                if (answerDto == null) return;

                Long presentQuizId = quizDataCenter.getPresentQuizDto().getQuizId();
                Long quizIdInAnswerDto = answerDto.getQuizId();
                if (!presentQuizId.equals(quizIdInAnswerDto)) return;

                // 엑세스 토큰을 가진 세션의 경우 memberId 값이 있음
                Long memberId = (Long) session.getAttributes().get("memberId");
                answerDto.setMemberId(memberId);
                quizDataCenter.loadAnswerFromUser(session.getId(), answerDto);
                TextMessage guideMessage =
                        textMessageFactory.produceTextMessage(guideMessageBundle.getAnswerSubmittedMessage());
                session.sendMessage(guideMessage);
                return;
            }
        }
    }

    /*
    설명:
    WebSocketMessage은 두 타입을 가진다. BinaryMessage /  TextMessage
    우선 현재 서비스에서 사용되는 타입은 텍스트메시지이므로 TextMessage만 Handler하도록 구현한다.

    추후에 기능 확장을 위해 이진메시지도 다뤄야하므로
    TextMessageHandler / BinaryMessageHandler를 WebSocketQuizHandler와 분리

    TODO LIST

    처리할 수 있는 객체 타입 Bundle을 만들고 contains로 하면 판단해서 더 다양한 메시지를 처리해보도록 하자. 현재는 AnswerDto만 처리한다.
    TODO TextMessageHandler 구현

    TODO BinaryMessageHandler 구현
     */

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.sendMessage(textMessageFactory.produceTextMessage(
                guideMessageBundle.getErrorMessage()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session.getId());
        if (sessions.isEmpty()) {
            presentState = WAITING;
            quizDataCenter.setPresentQuiz(null);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}