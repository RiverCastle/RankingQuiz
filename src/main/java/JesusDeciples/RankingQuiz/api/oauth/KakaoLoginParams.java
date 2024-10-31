package JesusDeciples.RankingQuiz.api.oauth;

import JesusDeciples.RankingQuiz.api.enums.OAuthProvider;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class KakaoLoginParams implements OAuth2LoginParams {
    private String authorizationCode;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> createRequestBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", authorizationCode);
        return body;
    }
}
