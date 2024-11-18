package JesusDeciples.RankingQuiz.api.webSocket.messageHandler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StopWatch;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class QuizMessageSendTest {
    private static final Map<String, WebSocketSession> sessions_200 = new HashMap<>();
    private static final Map<String, WebSocketSession> sessions_1000 = new HashMap<>();
    private static final Map<String, WebSocketSession> sessions_10000 = new HashMap<>();
    private static final Map<String, WebSocketSession> sessions_30000 = new HashMap<>();
    private static final Map<String, WebSocketSession> sessions_50000 = new HashMap<>();

    @BeforeAll
    static void setUp() {
        for (int i = 0; i < 200; i++) {
            WebSocketSession session = Mockito.mock(WebSocketSession.class);
            sessions_200.put(session.getId() + i, session);
        }
        for (int i = 0; i < 1000; i++) {
            WebSocketSession session = Mockito.mock(WebSocketSession.class);
            sessions_1000.put(session.getId() + i, session);
        }
        for (int i = 0; i < 10000; i++) {
            WebSocketSession session = Mockito.mock(WebSocketSession.class);
            sessions_10000.put(session.getId() + i, session);
        }
        for (int i = 0; i < 30000; i++) {
            WebSocketSession session = Mockito.mock(WebSocketSession.class);
            sessions_30000.put(session.getId() + i, session);
        }
        for (int i = 0; i < 50000; i++) {
            WebSocketSession session = Mockito.mock(WebSocketSession.class);
            sessions_50000.put(session.getId() + i, session);
        }
    }

    @Test
    void testMessageSendTime() throws IOException, InterruptedException {
        sendMessages(sessions_200);
        sendMessages(sessions_1000);
        sendMessages(sessions_10000);
        sendMessages(sessions_30000);
        sendMessages(sessions_50000);
    }
    /*
    테스트 목적: 동일한 퀴즈 데이터 메시지를 세션에 전송할 때, 현재 순차적으로 전송하는데, 소요되는 시간을 측정하기 위함
    테스트 결과:
        세션 수 = 200:   0.0078525 seconds
        세션 수 = 1000:  0.022955  seconds
        세션 수 = 10000: 0.2079455 seconds
        세션 수 = 30000: 0.624372  seconds
        세션 수 = 50000: 1.0402312 seconds
     */

    private void sendMessages(Map<String, WebSocketSession> sessions) throws IOException, InterruptedException {
        StopWatch stopWatch = new StopWatch();
        Set<String> sessionIds = sessions.keySet();

        stopWatch.start("세션 수 = " + sessions.size());
        for (String sessionId : sessionIds) {
            sessions.get(sessionId).sendMessage(new TextMessage("Test Message"));
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    @Test
    @Async
    void testAsynMessageSendTime() throws IOException {
        sendMessagesAsync(sessions_200);
        sendMessagesAsync(sessions_1000);
        sendMessagesAsync(sessions_10000);
        sendMessagesAsync(sessions_30000);
        sendMessagesAsync(sessions_50000);
    }
    /*
    테스트 목적: 순차적으로 전송하지 않고, 스레드풀을 사용해 비동기적으로 전송했을 때 시간 측정
    테스트 결과:
        세션 수 = 200:   0.0078525 -> 0.0012984 seconds
        세션 수 = 1000:  0.022955  -> 0.0015127 seconds
        세션 수 = 10000: 0.2079455 -> 0.0041229 seconds
        세션 수 = 30000: 0.624372  -> 0.0099724 seconds
        세션 수 = 50000: 1.0402312 -> 0.0145439 seconds
     */

    private void sendMessagesAsync(Map<String, WebSocketSession> sessions) throws IOException {
        Set<String> sessionIds = sessions.keySet();
        TextMessage textMessage = new TextMessage("테스트 메시지");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("스레드 풀 생성 시작");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        stopWatch.stop();

        stopWatch.start("세션 수 = " + sessions.size());
        for (String sessionId : sessionIds) {
            executor.submit(new MessageSendingTask(sessions.get(sessionId), textMessage));
        }
        executor.shutdown();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}