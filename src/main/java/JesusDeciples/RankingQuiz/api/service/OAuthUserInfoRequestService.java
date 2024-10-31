package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.enums.OAuthProvider;
import JesusDeciples.RankingQuiz.api.oauth.OAuth2LoginParams;
import JesusDeciples.RankingQuiz.api.oauth.OAuthApiRequestSender;
import JesusDeciples.RankingQuiz.api.oauth.OAuthUserInfoResponse;
import JesusDeciples.RankingQuiz.api.oauth.ProviderApiTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthUserInfoRequestService {
    private final Map<OAuthProvider, OAuthApiRequestSender> requestSenders;
    public OAuthUserInfoResponse requestUserInfo(OAuth2LoginParams params) {
        OAuthApiRequestSender requestSender = requestSenders.get(params.oAuthProvider());

        ProviderApiTokens providerApiTokens = requestSender.requestTokens(params);
        OAuthUserInfoResponse response = requestSender.requestUserInfo(providerApiTokens.getAccessToken());
        return response;
    }
}
