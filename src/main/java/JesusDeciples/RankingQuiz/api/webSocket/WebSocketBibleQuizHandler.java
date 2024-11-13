package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.GuideMessage;
import JesusDeciples.RankingQuiz.api.dto.GuideMessageBundle;
import JesusDeciples.RankingQuiz.api.dto.MessageWrapper;
import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class WebSocketBibleQuizHandler implements WebSocketHandler {
    private final Long waitingTime = 3000L;
    private final CustomTextMessageFactory textMessageFactory;
    private final BibleQuizDataCenterMediator bibleQuizDataCenterMediator;
    private final Map<String, WebSocketSession> sessions = new HashMap<>();
    private final GuideMessageBundle guideMessageBundle;
    private final ObjectMapper objectMapper;
    private final AccessTokenMessageHandler accessTokenMessageHandler;
    private final AnswerDtoMessageHandler answerDtoMessageHandler;

    @Scheduled(fixedDelay = 1000)
    private void abcd() throws IOException, InterruptedException {
        DataCenterState presentState = bibleQuizDataCenterMediator.getQuizDataCenterState();
        if (presentState instanceof COMPLETE_SCORE) {
            sendQuizResultMessage();
            Thread.sleep(waitingTime);
            bibleQuizDataCenterMediator.updateDataCenterStateAndAction(new INIT_NEXT_QUIZ());
        } else if (presentState instanceof INIT_QUIZ || presentState instanceof INIT_NEXT_QUIZ) {
            bibleQuizDataCenterMediator.updateDataCenterStateAndAction(new ON_QUIZ());
            // 퀴즈 메시지 전송
            QuizDto quizDto = bibleQuizDataCenterMediator.getPresentQuizDto();
            TextMessage quizMessage = textMessageFactory.produceTextMessage(quizDto);
            sendMessageToAllSessions(quizMessage);
        } else { //COMPLETE_QUIZ, InIt SCORE, INIT_SCORE, ONQUIZ, WAITING
            bibleQuizDataCenterMediator.updateDataCenterStateAndAction(presentState);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        DataCenterState presentState = bibleQuizDataCenterMediator.getQuizDataCenterState();
        sessions.put(session.getId(), session);
        // 대기상태 중 새 세션
        if (presentState instanceof WAITING) {
            bibleQuizDataCenterMediator.updateDataCenterStateAndAction(new INIT_QUIZ());
        }
        session.sendMessage(textMessageFactory.produceTextMessage(guideMessageBundle.getPrepareMessage()));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        DataCenterState presentState = bibleQuizDataCenterMediator.getQuizDataCenterState();
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
                if (!(presentState instanceof COMPLETE_QUIZ || presentState instanceof ON_QUIZ)) return;
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
            bibleQuizDataCenterMediator.updateDataCenterStateAndAction(new WAITING());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void sendQuizResultMessage() throws IOException {
        Set<String> sessionIds = sessions.keySet(); // 접속 세션 IDs
        Map<String, QuizResultDto> results = bibleQuizDataCenterMediator.getQuizResults();
        Set<String> sessionIdsOfParticipants = results.keySet(); // 퀴즈에 참여한 세션 ID

        String winner = (bibleQuizDataCenterMediator.getQuizWinnerName() == null) ?
                "없습니다." : bibleQuizDataCenterMediator.getQuizWinnerName() + "님입니다.";
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
