package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.jwt.JWTProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenMessageHandler {
    private final JWTProducer jwtProducer;
    public Long handleAccessTokenMessageObject(Object objectInMessage) {
        return Long.parseLong(jwtProducer.extractSubject(objectInMessage.toString()));
    }
    /*
    MessageWrapper에 담긴 AccessToken(messageObject)을 받아
    memberId 반환
    WebSocketMessageHandler에서 세션에 memberId 속성 추가
     */
}
