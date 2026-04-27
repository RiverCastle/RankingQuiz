package JesusDeciples.RankingQuiz.api.oauth;

public interface OAuthApiRequestSender {
    ProviderApiTokens requestTokens(OAuth2LoginParams params);

    OAuthUserInfoResponse requestUserInfo(String accessToken);
}
