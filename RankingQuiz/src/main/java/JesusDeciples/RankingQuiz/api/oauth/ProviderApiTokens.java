package JesusDeciples.RankingQuiz.api.oauth;

import JesusDeciples.RankingQuiz.api.enums.OAuthProvider;

public interface ProviderApiTokens {
    OAuthProvider getProvider();
    String getAccessToken();
}
