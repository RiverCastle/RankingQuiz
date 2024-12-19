package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.oauth.AuthTokens;
import JesusDeciples.RankingQuiz.api.oauth.KakaoLoginParams;
import JesusDeciples.RankingQuiz.api.service.OAuth2LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/code")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
@Tag(name = "AuthToken API", description = "인증 토큰 관련 API 입니다.")
public class AuthController {
    private final OAuth2LoginService oAuth2LoginService;

    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인 인증 토큰 발급",
                description = "카카오 계정을 사용하여 로그인하고 인증 토큰을 발급받습니다.")
    public ResponseEntity<AuthTokens> LoginUsingKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuth2LoginService.login(params));
    }
    /*
    1. 프론트엔드 서버로부터 Authorization code 받
    2. OAuth 로그인 수행
    3. AuthToken 발급
    */
}
