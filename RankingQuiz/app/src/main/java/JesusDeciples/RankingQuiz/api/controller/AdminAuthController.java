package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.enums.Authority;
import JesusDeciples.RankingQuiz.api.jwt.JWTProducer;
import JesusDeciples.RankingQuiz.api.oauth.AuthTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/admin")
@CrossOrigin(origins = {"http://localhost:8081", "https://rankingquiz.rivercastleworks.site"})
public class AdminAuthController {

    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final long EXPIRES_IN_MS = 1000L * 60 * 60 * 24; // 24시간

    private final JWTProducer jwtProducer;

    @PostMapping
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> body) {
        String id = body.get("id");
        String password = body.get("password");

        if (!ADMIN_ID.equals(id) || !ADMIN_PASSWORD.equals(password)) {
            return ResponseEntity.status(401).body("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        Date expiredAt = new Date(System.currentTimeMillis() + EXPIRES_IN_MS);
        String accessToken = jwtProducer.produceJwt(ADMIN_ID, Authority.ROLE_ADMIN, expiredAt);

        return ResponseEntity.ok(AuthTokens.of(accessToken, "Bearer", EXPIRES_IN_MS));
    }
}
