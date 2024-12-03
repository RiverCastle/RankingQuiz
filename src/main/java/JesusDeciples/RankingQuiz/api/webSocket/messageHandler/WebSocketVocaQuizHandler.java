package JesusDeciples.RankingQuiz.api.webSocket.messageHandler;

import JesusDeciples.RankingQuiz.api.dto.GuideMessage;
import JesusDeciples.RankingQuiz.api.dto.GuideMessageBundle;
import JesusDeciples.RankingQuiz.api.dto.MessageWrapper;
import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.*;
import JesusDeciples.RankingQuiz.api.webSocket.CustomTextMessageFactory;
import JesusDeciples.RankingQuiz.api.webSocket.QuizDataCenterMediator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.*;


@Component
@RequiredArgsConstructor
public class WebSocketVocaQuizHandler implements WebSocketHandler {
    private final QuizCategory category = QuizCategory.ENG_VOCA;
    private final Long waitingTime = 3000L;
    private final CustomTextMessageFactory textMessageFactory;
    private final QuizDataCenterMediator quizDataCenterMediator;
    private final Map<String, WebSocketSession> sessions = new HashMap<>();
    private final GuideMessageBundle guideMessageBundle;
    private final ObjectMapper objectMapper;
    private final AccessTokenMessageHandler accessTokenMessageHandler;

    @Scheduled(fixedDelay = 1000)
    private void abcd() throws IOException, InterruptedException {
        DataCenterState presentState = quizDataCenterMediator.getQuizDataCenterState(category);
        if (presentState instanceof COMPLETE_SCORE) {
            sendQuizResultMessage();
            Thread.sleep(waitingTime);
            quizDataCenterMediator.updateDataCenterStateAndAction(category, new INIT_NEXT_QUIZ());
        } else if (presentState instanceof INIT_QUIZ || presentState instanceof INIT_NEXT_QUIZ) {
            quizDataCenterMediator.updateDataCenterStateAndAction(category, new ON_QUIZ());
            // 퀴즈 메시지 전송
            QuizDto quizDto = quizDataCenterMediator.getPresentQuizDto(category);
            TextMessage quizMessage = textMessageFactory.produceTextMessage(quizDto);
            sendMessageToAllSessions(quizMessage);
        } else { //COMPLETE_QUIZ, InIt SCORE, INIT_SCORE, ONQUIZ, WAITING
            quizDataCenterMediator.updateDataCenterStateAndAction(category, presentState);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        DataCenterState presentState = quizDataCenterMediator.getQuizDataCenterState(category);
        sessions.put(session.getId(), session);
        // 대기상태 중 새 세션
        if (presentState instanceof WAITING) {
            quizDataCenterMediator.updateDataCenterStateAndAction(category, new INIT_QUIZ());
        }
        session.sendMessage(textMessageFactory.produceTextMessage(guideMessageBundle.getPrepareMessage()));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        DataCenterState presentState = quizDataCenterMediator.getQuizDataCenterState(category);
        if (message instanceof BinaryMessage) return;

        MessageWrapper messageWrapperFromClient = objectMapper.readValue(((TextMessage) message).getPayload(), MessageWrapper.class);
        String dataType = messageWrapperFromClient.getDataType();
        Object objectInMessage = messageWrapperFromClient.getObject();

        switch (dataType) {
            case "AccessToken" -> {
                Long memberId =
                        accessTokenMessageHandler.handleAccessTokenMessageObject(objectInMessage);

                boolean connectionCheckResult = memberId == null ? true : checkConnectionAlready(session, memberId);
                if (connectionCheckResult) session.getAttributes().put("memberId", memberId);
            }

            case "AnswerDto" -> {
                if (!(presentState instanceof COMPLETE_QUIZ || presentState instanceof ON_QUIZ)) return;
                Long memberId = (Long) session.getAttributes().get("memberId");
                quizDataCenterMediator.sendAnswerToDataCenter(category, session.getId(), memberId, objectInMessage);
                TextMessage guideMessage =
                        textMessageFactory.produceTextMessage(guideMessageBundle.getAnswerSubmittedMessage());
                session.sendMessage(guideMessage);
                return;
            }
        }
    }

    private boolean checkConnectionAlready(WebSocketSession session, Long memberId) throws IOException {
        for (WebSocketSession presentSession : sessions.values()) {
            if (presentSession.getAttributes().get("memberId") == memberId) {
                sendMessageToSpecificSession(new TextMessage("이미 해당 퀴즈에 참여중입니다. 오류라면 에러 피드백을 남겨주세요. 비정상적인 접속으로 여겨져 접속이 종료됩니다."), session);
                session.close();
                sendMessageToSpecificSession(new TextMessage("또 다른 접속이 감지되었습니다. 본인이 아니라면 암호를 바꾸시길 권장드립니다. 비정상적인 접속으로 여겨져 접속이 종료됩니다."), presentSession);
                presentSession.close();
                return false;
            }
        }
        return true;
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
            quizDataCenterMediator.updateDataCenterStateAndAction(category, new WAITING());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void sendQuizResultMessage() throws IOException {
        Set<String> sessionIds = sessions.keySet(); // 접속 세션 IDs
        Map<String, QuizResultDto> results = quizDataCenterMediator.getQuizResults(category);
        Set<String> sessionIdsOfParticipants = results.keySet(); // 퀴즈에 참여한 세션 ID

        String winner = (quizDataCenterMediator.getQuizWinnerName(category) == null) ?
                "없습니다." : quizDataCenterMediator.getQuizWinnerName(category) + "님입니다.";
        GuideMessage winner_notification = new GuideMessage("이번 퀴즈의 우승자는 " + winner);
        winner_notification.setDisplay(true);
        guideMessageBundle.setWinner_notification(winner_notification);
        TextMessage winnerAnouncementTextMessage =
                textMessageFactory.produceTextMessage(guideMessageBundle.getWinner_notification());

        for (String sessionId : sessionIds) {// 전체 세션
            if (sessionIdsOfParticipants.contains(sessionId)) {
                // 활성 참가자 중 이전 퀴즈 참가자에게 퀴즈 결과 전송
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
    }
}