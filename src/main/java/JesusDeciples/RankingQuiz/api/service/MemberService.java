package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.Member;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    Member findOrCreateMemberByEmail(String email);
}
