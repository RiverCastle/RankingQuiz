package JesusDeciples.RankingQuiz.api.oauth;

import JesusDeciples.RankingQuiz.api.enums.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuth2LoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> createRequestBody();
}
