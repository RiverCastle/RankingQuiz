package JesusDeciples.RankingQuiz.api.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthTokens {
    private String accessToken;
    private String grantType;
    private Long expiresIn;

    public static AuthTokens of(String accessToken, String grantType, Long expiresIn) {
        return new AuthTokens(accessToken, grantType, expiresIn);
    }
}
