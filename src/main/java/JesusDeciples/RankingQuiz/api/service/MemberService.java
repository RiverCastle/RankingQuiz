package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.response.MemberInfoResponseDto;
import JesusDeciples.RankingQuiz.api.entity.Member;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    Member findOrCreateMemberByEmail(String email);

    MemberInfoResponseDto findMemberInfoById(Long currentMemberId);

    void chageMemberNameInto(Long memberId, String newName);
}
