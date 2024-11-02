package JesusDeciples.RankingQuiz.api.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {}

    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null | authentication.getName() == null) {
            throw new RuntimeException("인증 정보가 없음");
        }
        return Long.parseLong(authentication.getName());
    }
}