package JesusDeciples.RankingQuiz.api.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoApiRequestSenderImpl implements OAuthApiRequestSender {
    @Value("${KAKAO_CLIENT_ID}")
    private String clientId;
    @Value("${KAKAO_CLIENT-SECRET}")
    private String secret;
    @Value("${KAKAO_REDIRECT_URI}")
    private String redirectUri;
    @Value("${KAKAO_AUTH_URL}")
    private String authUrl;
    @Value("${KAKAO_API_URL}")
    private String apiUrl;

    private final String grantType = "authorization_code";
    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public ProviderApiTokens requestTokens(OAuth2LoginParams params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.createRequestBody(); // Map<String, String> body = new HashMap<>(); 사용불가 body 구성에 부적합
        body.add("redirect_uri", redirectUri);
        body.add("client_id", clientId);
        body.add("client_secret", secret);

        HttpEntity<?> request = new HttpEntity<>(body, headers);
        String kakaoTokenUrl = authUrl + "/oauth/token";

        KakaoApiTokensImpl kakaoApiTokens = restTemplate.postForObject(kakaoTokenUrl, request, KakaoApiTokensImpl.class);

        assert kakaoApiTokens != null;
        return kakaoApiTokens;
    }
    /*
    Authorization Code를 사용해 사용자 정보 AccessToken 발급
     */

    @Override
    public OAuthUserInfoResponse requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(); // Map<String, String> body = new HashMap<>(); 사용불가 body 구성에 부적합
        body.add("property_keys", "[\"kakao_account.email\"]"); // 필수 X, 지워도 데이터가 오는지 확인해보기

        HttpEntity<?> request = new HttpEntity<>(body, headers);
        String kakaoUserInfoUrl = apiUrl + "/v2/user/me";
        KakaoUserInfoResponseImpl response = restTemplate.postForObject(kakaoUserInfoUrl, request, KakaoUserInfoResponseImpl.class);
        return response;
    }
    /*
    AccessToken으로 UserInfo 요청하기
     */

}
