package JesusDeciples.RankingQuiz.api.webSocket.messageHandler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class MessageSendingTask implements Runnable {
    private WebSocketSession session;
    private TextMessage textMessage;

    public MessageSendingTask(WebSocketSession session, TextMessage textMessage) {
        this.session = session;
        this.textMessage = textMessage;
    }

    @Override
    public void run() {
        try {
            session.sendMessage(textMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
