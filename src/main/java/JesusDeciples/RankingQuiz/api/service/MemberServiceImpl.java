package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.Member;
import JesusDeciples.RankingQuiz.api.enums.OAuthProvider;
import JesusDeciples.RankingQuiz.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static JesusDeciples.RankingQuiz.api.enums.Authority.ROLE_USER;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    @Override
    public Member findOrCreateMemberByEmail(String email) {
        Optional<Member> optional = memberRepository.findByEmail(email);
        if (optional.isPresent()) return optional.get();
        else {
            String name = email.split("@")[0];
            Member newMember = new Member();
            newMember.setEmail(email);
            newMember.setName(name);
            newMember.setPoint(0);
            newMember.setAuthority(ROLE_USER);
            newMember.setProvider(OAuthProvider.KAKAO);
            return memberRepository.save(newMember);
        }
    }
}
