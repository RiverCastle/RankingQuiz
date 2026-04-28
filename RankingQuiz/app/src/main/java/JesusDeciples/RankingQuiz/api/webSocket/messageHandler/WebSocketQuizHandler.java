package JesusDeciples.RankingQuiz.api.webSocket.messageHandler;

import JesusDeciples.RankingQuiz.api.admin.service.QuizStatusService;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketQuizHandler implements WebSocketHandler {

    private final QuizCategory category;
    private final Long waitingTime = 3000L;
    private final CustomTextMessageFactory textMessageFactory;
    private final QuizDataCenterMediator quizDataCenterMediator;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final GuideMessageBundle guideMessageBundle;
    private final ObjectMapper objectMapper;
    private final AccessTokenMessageHandler accessTokenMessageHandler;
    private final QuizStatusService quizStatusService;

    public WebSocketQuizHandler(
            QuizCategory category,
            CustomTextMessageFactory textMessageFactory,
            QuizDataCenterMediator quizDataCenterMediator,
            GuideMessageBundle guideMessageBundle,
            ObjectMapper objectMapper,
            AccessTokenMessageHandler accessTokenMessageHandler,
            QuizStatusService quizStatusService) {
        this.category = category;
        this.textMessageFactory = textMessageFactory;
        this.quizDataCenterMediator = quizDataCenterMediator;
        this.guideMessageBundle = guideMessageBundle;
        this.objectMapper = objectMapper;
        this.accessTokenMessageHandler = accessTokenMessageHandler;
        this.quizStatusService = quizStatusService;
    }

    @Scheduled(fixedDelay = 1000)
    private void tick() throws IOException, InterruptedException {
        DataCenterState presentState = quizDataCenterMediator.getQuizDataCenterState(category);
        if (presentState instanceof COMPLETE_SCORE) {
            sendQuizResultMessage();
            Thread.sleep(waitingTime);
            quizDataCenterMediator.updateDataCenterStateAndAction(category, new INIT_NEXT_QUIZ());
        } else if (presentState instanceof INIT_QUIZ || presentState instanceof INIT_NEXT_QUIZ) {
            quizDataCenterMediator.updateDataCenterStateAndAction(category, new ON_QUIZ());
            QuizDto quizDto = quizDataCenterMediator.getPresentQuizDto(category);
            TextMessage quizMessage = textMessageFactory.produceTextMessage(quizDto);
            sendMessageToAllSessions(quizMessage);
        } else {
            quizDataCenterMediator.updateDataCenterStateAndAction(category, presentState);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        if (!quizStatusService.getStatus(category)) {
            GuideMessage disabledMsg = new GuideMessage(
                    "현재 " + category.getDisplayName() + "는 준비 중입니다. 잠시 후 다시 시도해주세요.");
            disabledMsg.setDisplay(true);
            session.sendMessage(textMessageFactory.produceTextMessage(disabledMsg));
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        DataCenterState presentState = quizDataCenterMediator.getQuizDataCenterState(category);
        sessions.put(session.getId(), session);
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
                Long memberId = accessTokenMessageHandler.handleAccessTokenMessageObject(objectInMessage);
                boolean ok = memberId == null || checkConnectionAlready(session, memberId);
                if (ok) session.getAttributes().put("memberId", memberId);
            }
            case "AnswerDto" -> {
                if (!(presentState instanceof COMPLETE_QUIZ || presentState instanceof ON_QUIZ)) return;
                Long memberId = (Long) session.getAttributes().get("memberId");
                quizDataCenterMediator.sendAnswerToDataCenter(category, session.getId(), memberId, objectInMessage);
                session.sendMessage(textMessageFactory.produceTextMessage(guideMessageBundle.getAnswerSubmittedMessage()));
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.sendMessage(textMessageFactory.produceTextMessage(guideMessageBundle.getErrorMessage()));
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

    public int getSessionCount() {
        return sessions.size();
    }

    public QuizCategory getCategory() {
        return category;
    }

    private boolean checkConnectionAlready(WebSocketSession session, Long memberId) throws IOException {
        for (WebSocketSession presentSession : sessions.values()) {
            if (memberId.equals(presentSession.getAttributes().get("memberId"))) {
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

    private void sendQuizResultMessage() throws IOException {
        Set<String> sessionIds = sessions.keySet();
        Map<String, QuizResultDto> results = quizDataCenterMediator.getQuizResults(category);
        Set<String> sessionIdsOfParticipants = results.keySet();

        String winnerName = quizDataCenterMediator.getQuizWinnerName(category);
        String winnerText = winnerName == null ? "없습니다." : winnerName + "님입니다.";
        GuideMessage winnerNotification = new GuideMessage("이번 퀴즈의 우승자는 " + winnerText);
        winnerNotification.setDisplay(true);
        TextMessage winnerAnnouncementMessage = textMessageFactory.produceTextMessage(winnerNotification);

        for (String sessionId : sessionIds) {
            WebSocketSession session = sessions.get(sessionId);
            if (session == null || !session.isOpen()) continue;

            if (sessionIdsOfParticipants.contains(sessionId)) {
                session.sendMessage(textMessageFactory.produceTextMessage(results.get(sessionId)));
                session.sendMessage(winnerAnnouncementMessage);
            } else {
                GuideMessage notParticipated = new GuideMessage(
                        guideMessageBundle.getNotParticipatedMessage().getMessage());
                notParticipated.setDisplay(true);
                session.sendMessage(textMessageFactory.produceTextMessage(notParticipated));
            }
        }
    }
}
