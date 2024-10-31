package JesusDeciples.RankingQuiz.api.oauth;

import JesusDeciples.RankingQuiz.api.jwt.JWTProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokensProducer {
    private static final String tokenType = "Bearer";
    private static final long accessTokenExpireIn = 1000 * 60 * 60;
    private static final long refreshTokenExpireIn = 1000 * 60 * 60 * 3;
    private final JWTProducer jwtProducer;

    public AuthTokens produceAuthTokens(Long memberId) {
        long now = (new Date()).getTime();
        Date accessTokenExpireAt = new Date(now + accessTokenExpireIn);
        Date refreshTokenExpireAt = new Date(now + refreshTokenExpireIn);

        String subject = memberId.toString();
        String accessToken = jwtProducer.produceJwt(subject, accessTokenExpireAt);
        String refreshTokne = jwtProducer.produceJwt(subject, refreshTokenExpireAt);

        return AuthTokens.of(accessToken, refreshTokne, tokenType, accessTokenExpireIn);
    }

    public Long extractMemberId(String accessToken) {
        return Long.valueOf(jwtProducer.extractSubject(accessToken));
    }
}
