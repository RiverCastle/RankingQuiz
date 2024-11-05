package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.GuideMessage;
import JesusDeciples.RankingQuiz.api.dto.GuideMessageBundle;
import JesusDeciples.RankingQuiz.api.dto.MessageWrapper;
import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static JesusDeciples.RankingQuiz.api.webSocket.QuizDataCenterState.*;

@Component
@RequiredArgsConstructor
public class WebSocketQuizHandler implements WebSocketHandler {
    private final Long waitingTime = 3L; // 퀴즈 수거 대기 시간
    private final CustomTextMessageFactory textMessageFactory;
    private final QuizDataCenterMediator quizDataCenterMediator;
    private final Map<String, WebSocketSession> sessions = new HashMap<>();
    private final GuideMessageBundle guideMessageBundle;
    private final ObjectMapper objectMapper;
    private final AccessTokenMessageHandler accessTokenMessageHandler;
    private final AnswerDtoMessageHandler answerDtoMessageHandler;

    @Scheduled(fixedDelay = 1000)
    private void checkPresentState() throws IOException, InterruptedException {
        QuizDataCenterState presentState = quizDataCenterMediator.getQuizDataCenterState();
        switch (presentState) {
            case ON_QUIZ -> {
                LocalDateTime quizFinishedAt = quizDataCenterMediator.getPresentQuizDto().getFinishedAt().minusSeconds(2L);
                LocalDateTime now = LocalDateTime.now();

                if (now.isAfter(quizFinishedAt)) {
                    updateQuizSystemState(COMPLETED_QUIZ_GETTING_ANSWERED);
                }
                break;
            }
            case COMPLETED_QUIZ_GETTING_ANSWERED -> {
                LocalDateTime collectingAnswersFinishedAt = quizDataCenterMediator.getPresentQuizDto().getFinishedAt().plusSeconds(waitingTime);
                LocalDateTime now = LocalDateTime.now();
                if (now.isAfter(collectingAnswersFinishedAt)) {
                    updateQuizSystemState(ON_SCORING);
                }
                break;
            }
            case COMPLETED_SCORING -> {
                updateQuizSystemState(COMPLETED_SCORING);
            }
        }
    }

    public void updateQuizSystemState(QuizDataCenterState nextState) throws IOException, InterruptedException {
        QuizDataCenterState presentState = quizDataCenterMediator.getQuizDataCenterState();

        if (presentState == ON_QUIZ & nextState == COMPLETED_QUIZ_GETTING_ANSWERED) {
            // 퀴즈 중 -> 퀴즈 종료, 답안 수거로 상태 변경
            quizDataCenterMediator.updateQuizDataCenterState(nextState);
        } else if (presentState == COMPLETED_QUIZ_GETTING_ANSWERED & nextState == ON_SCORING) {
            // 퀴즈 종료, 답안 수거 -> 채점 시작
            quizDataCenterMediator.updateQuizDataCenterState(nextState);
        } else if (nextState == COMPLETED_SCORING) {
            // 채점 시작 -> 채점 종료
            quizDataCenterMediator.updateQuizDataCenterState(nextState);

            Set<String> sessionIds = sessions.keySet(); // 접속 세션 IDs
            Map<String, QuizResultDto> results = quizDataCenterMediator.getQuizResults(); // 참여자 별 퀴즈 결과
            Set<String> sessionIdsOfParticipants = results.keySet(); // 퀴즈에 참여한 세션 ID


            String winner = (quizDataCenterMediator.getQuizWinnerName() == null) ? "없습니다." : quizDataCenterMediator.getQuizWinnerName() + "님입니다.";
            GuideMessage winner_notification = new GuideMessage("이번 퀴즈의 우승자는 " + winner);
            winner_notification.setDisplay(true);
            guideMessageBundle.setWinner_notification(winner_notification);
            TextMessage winnerAnouncementTextMessage =
                    textMessageFactory.produceTextMessage(guideMessageBundle.getWinner_notification());


            // 전체 세션
            for (String sessionId : sessionIds) {
                if (sessionIdsOfParticipants.contains(sessionId)) {// 활성 참가자 중 이전 퀴즈 참가자에게 퀴즈 결과 전송
                    TextMessage textMessage = // QuizResult -> TextMessage 변환
                            textMessageFactory.produceTextMessage(results.get(sessionId));
                    sessions.get(sessionId).sendMessage(textMessage);
                    sessions.get(sessionId).sendMessage(winnerAnouncementTextMessage);
                } else {
                    GuideMessage guideMessage = guideMessageBundle.getNotParticipatedMessage();
                    guideMessage.setDisplay(true);
                    TextMessage message =
                            textMessageFactory.produceTextMessage(guideMessage);
                    sessions.get(sessionId).sendMessage(message);
                }
            }
            Thread.sleep(3000);
            updateQuizSystemState(ON_QUIZ_SETTING);
        } else if (presentState == COMPLETED_SCORING & nextState == ON_QUIZ_SETTING) {
            // 채점 종료 -> 새 퀴즈 생성
            // 전체 세션에 새 퀴즈 전송
            quizDataCenterMediator.updateQuizDataCenterState(nextState);
            QuizDto quizDto = quizDataCenterMediator.getPresentQuizDto();
            TextMessage newQuizTextMessage = // 새 퀴즈 메시지로 변환
                    textMessageFactory.produceTextMessage(quizDto);

            Set<String> sessionIds = sessions.keySet();
            for (String sessionId : sessionIds) // 전체 활성 참가자에게 전송
                sessions.get(sessionId).sendMessage(newQuizTextMessage);
            quizDataCenterMediator.updateQuizDataCenterState(ON_QUIZ);
        } else if (presentState == WAITING & nextState == ON_QUIZ_SETTING) {
            // 대기 -> 퀴즈 생성
            // 전체 세션에 새 퀴즈 메시지 전송
            quizDataCenterMediator.updateQuizDataCenterState(nextState);
            QuizDto quizDto = quizDataCenterMediator.getPresentQuizDto();
            TextMessage newQuizTextMessage = // 새 퀴즈 메시지로 변환
                    textMessageFactory.produceTextMessage(quizDto);
            sendMessageToAllSessions(newQuizTextMessage);
            quizDataCenterMediator.updateQuizDataCenterState(ON_QUIZ);
        } else if (presentState == ON_QUIZ_SETTING & nextState == ON_QUIZ) {
            // 퀴즈 생성 완료 -> 퀴즈 시작
            quizDataCenterMediator.updateQuizDataCenterState(nextState);
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException, InterruptedException {
        QuizDataCenterState presentState = quizDataCenterMediator.getQuizDataCenterState();
        sessions.put(session.getId(), session);
        // 대기상태 중 새 세션
        if (presentState == WAITING) {
            updateQuizSystemState(ON_QUIZ_SETTING);
        }
        session.sendMessage(textMessageFactory.produceTextMessage(guideMessageBundle.getPrepareMessage()));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        QuizDataCenterState presentState = quizDataCenterMediator.getQuizDataCenterState();
        if (message instanceof BinaryMessage) return;

        MessageWrapper messageWrapperFromClient = objectMapper.readValue(((TextMessage) message).getPayload(), MessageWrapper.class);
        String dataType = messageWrapperFromClient.getDataType();
        Object objectInMessage = messageWrapperFromClient.getObject();

        switch (dataType) {
            case "AccessToken" -> {
                Long memberId =
                        accessTokenMessageHandler.handleAccessTokenMessageObject(objectInMessage);
                session.getAttributes().put("memberId", memberId);
            }

            case "AnswerDto" -> {
                if (presentState != COMPLETED_QUIZ_GETTING_ANSWERED) return;
                Long memberId = (Long) session.getAttributes().get("memberId");
                answerDtoMessageHandler.handleAnswerDtoMessageObject(session.getId(), memberId, objectInMessage);
                TextMessage guideMessage =
                        textMessageFactory.produceTextMessage(guideMessageBundle.getAnswerSubmittedMessage());
                session.sendMessage(guideMessage);
                return;
            }
        }
    }

    private void sendMessageToAllSessions(TextMessage message) throws IOException {
        for (WebSocketSession session : sessions.values()) session.sendMessage(message);
    }

    private void sendMessageToSpecificSession(TextMessage message, WebSocketSession session) throws IOException {
        session.sendMessage(message);
    }

    private void sendMessageToSpecificSessionsGroup(TextMessage message, Set<String> sessionIds) throws IOException {
        for (String sessionId : sessionIds) sessions.get(sessionId).sendMessage(message);
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
        QuizDataCenterState presentState = quizDataCenterMediator.getQuizDataCenterState();
        sessions.remove(session.getId());
        if (sessions.isEmpty()) {
            quizDataCenterMediator.updateQuizDataCenterState(WAITING);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}