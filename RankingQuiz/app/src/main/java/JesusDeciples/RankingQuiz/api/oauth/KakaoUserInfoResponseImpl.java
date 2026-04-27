package JesusDeciples.RankingQuiz.api.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserInfoResponseImpl implements OAuthUserInfoResponse {
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;
    @Override
    public String getEmail() {
        return kakaoAccount.getEmail();
    }
}
