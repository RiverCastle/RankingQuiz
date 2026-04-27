package JesusDeciples.RankingQuiz.api.oauth;

import JesusDeciples.RankingQuiz.api.entity.Member;
import JesusDeciples.RankingQuiz.api.enums.Authority;
import JesusDeciples.RankingQuiz.api.jwt.JWTProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokensProducer {
    private static final String tokenType = "Bearer";
    private static final long accessTokenExpireIn = 1000 * 60 * 60;

    private final JWTProducer jwtProducer;

    public AuthTokens produceAuthTokens(Member member) {
        long now = (new Date()).getTime();
        Date accessTokenExpireAt = new Date(now + accessTokenExpireIn);

        Long memberId = member.getId();
        String subject = memberId.toString();
        Authority authority = member.getAuthority();
        String accessToken = jwtProducer.produceJwt(subject, authority, accessTokenExpireAt);

        return AuthTokens.of(accessToken, tokenType, accessTokenExpireIn);
    }

    public Long extractMemberId(String accessToken) {
        return Long.valueOf(jwtProducer.extractSubject(accessToken));
    }
}
