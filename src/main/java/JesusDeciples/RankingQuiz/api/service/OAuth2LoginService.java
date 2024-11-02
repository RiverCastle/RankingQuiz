package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.Member;
import JesusDeciples.RankingQuiz.api.oauth.AuthTokens;
import JesusDeciples.RankingQuiz.api.oauth.AuthTokensProducer;
import JesusDeciples.RankingQuiz.api.oauth.OAuth2LoginParams;
import JesusDeciples.RankingQuiz.api.oauth.OAuthUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {
    private final OAuthUserInfoRequestService oAuthUserInfoRequestService;
    private final MemberService memberService;
    private final AuthTokensProducer authTokensProducer;

    public AuthTokens login(OAuth2LoginParams params) {
        OAuthUserInfoResponse oAuthUserInfoResponse = oAuthUserInfoRequestService.requestUserInfo(params);
        Member member = memberService.findOrCreateMemberByEmail(oAuthUserInfoResponse.getEmail());
        return authTokensProducer.produceAuthTokens(member);
    }
}
