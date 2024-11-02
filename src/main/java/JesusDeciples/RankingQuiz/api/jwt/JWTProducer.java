package JesusDeciples.RankingQuiz.api.jwt;

import JesusDeciples.RankingQuiz.api.enums.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    /*
    JWT의 Paylaod가 Claims
    여기에 특정 주체에 대한 정보를 담는다.
    Subject / Role
    MemberId를 String으로 바꿔서 Subject로 사용
     */
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

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        String auth = claims.get("auth").toString();
        if (auth == null) throw new RuntimeException("서비스에 대한 권한이 없습니다. 피드백 페이지에 알려주세요.");

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(auth));
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}
