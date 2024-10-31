package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.oauth.AuthTokens;
import JesusDeciples.RankingQuiz.api.oauth.KakaoLoginParams;
import JesusDeciples.RankingQuiz.api.service.OAuth2LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/code")
public class AuthController {
    private final OAuth2LoginService oAuth2LoginService;

    @PostMapping("/kakao")
    @CrossOrigin(origins = "https://rankingquiz.rivercastleworks.site")
    public ResponseEntity<AuthTokens> LoginUsingKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuth2LoginService.login(params));
    }
    /*
    1. 프론트엔드 서버로부터 Authorization code 받
    2. OAuth 로그인 수행
    3. AuthToken 발급
    */
}
