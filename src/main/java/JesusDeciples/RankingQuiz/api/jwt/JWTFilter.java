package JesusDeciples.RankingQuiz.api.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTProducer jwtProducer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/quiz-content") ||
                requestURI.startsWith("/api/member") ||
                requestURI.startsWith("/api/quiz-results/my-results")) {
            String bearerToken = request.getHeader("Authorization");
            String jwt;
            if (bearerToken == null) jwt = null;
            else {
                if (bearerToken.startsWith("Bearer ")) {
                    jwt = bearerToken.substring(7);
                } else jwt = null;
            }
            Authentication authentication = jwtProducer.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
