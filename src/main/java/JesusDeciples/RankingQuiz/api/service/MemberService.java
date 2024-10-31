package JesusDeciples.RankingQuiz.api.service;

import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    Long findOrCreateMemberByEmail(String email);
}
