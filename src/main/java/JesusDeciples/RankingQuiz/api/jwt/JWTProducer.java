package JesusDeciples.RankingQuiz.api.jwt;

import JesusDeciples.RankingQuiz.api.enums.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JWTProducer {
    @Value("${SECRET}")
    private String key;

    public String produceJwt(String subject, Authority authority, Date expiredAt) {

        return Jwts.builder()
                .setSubject(subject)
                .claim("auth", authority)
                .setExpiration(expiredAt)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String extractSubject(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
