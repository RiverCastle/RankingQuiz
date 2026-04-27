package JesusDeciples.RankingQuiz.api.webSocket.messageHandler;

import JesusDeciples.RankingQuiz.api.jwt.JWTProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenMessageHandler {
    private final JWTProducer jwtProducer;

    public Long handleAccessTokenMessageObject(Object objectInMessage) {
        // [Fix] JWT 파싱 실패(만료·위조·형식오류) 시 예외를 잡아 null 반환 → 비회원 처리
        try {
            return Long.parseLong(jwtProducer.extractSubject(objectInMessage.toString()));
        } catch (Exception e) {
            return null;
        }
    }
}
