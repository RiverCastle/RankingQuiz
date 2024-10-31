package JesusDeciples.RankingQuiz.api.oauth;

import JesusDeciples.RankingQuiz.api.enums.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoApiTokensImpl implements ProviderApiTokens {
    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }
}
