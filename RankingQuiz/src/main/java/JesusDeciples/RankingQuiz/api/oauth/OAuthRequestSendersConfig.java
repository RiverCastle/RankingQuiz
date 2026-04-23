package JesusDeciples.RankingQuiz.api.oauth;

import JesusDeciples.RankingQuiz.api.enums.OAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
public class OAuthRequestSendersConfig {
    @Bean
    public Map<OAuthProvider, OAuthApiRequestSender> requestSenders(KakaoApiRequestSenderImpl kakaoApiRequestSender) {
                                                                    // 다른 OAuthApiRequestSender 구현체
        return Map.of(
                OAuthProvider.KAKAO, kakaoApiRequestSender
                // 다른 OAuthProvider, providerApiRequestSender
        );
    }
}
