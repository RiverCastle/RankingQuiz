package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.response.MemberInfoResponseDto;
import JesusDeciples.RankingQuiz.api.entity.Member;
import JesusDeciples.RankingQuiz.api.enums.OAuthProvider;
import JesusDeciples.RankingQuiz.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    @Override
    public Long findOrCreateMemberByEmail(String email) {
        Optional<Member> optional = memberRepository.findByEmail(email);
        if (optional.isPresent()) return optional.get().getId();
        else {
            String name = email.split("@")[0];
            Member newMember = new Member();
            newMember.setEmail(email);
            newMember.setName(name);
            newMember.setPoint(1000);
            newMember.setRole(MemberRole.USER);
            newMember.setProvider(OAuthProvider.KAKAO);
            return memberRepository.save(newMember).getId();
        }
    }

    @Override
    public MemberInfoResponseDto findMemberInfoById(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("회원가입 필요 | 유저 없음"));
        return MemberInfoResponseDto.of(member.getName(), member.getPoint());
    }

    @Override
    public void chageMemberNameInto(Long memberId, String newName) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("회원가입 필요 | 유저 없음"));
        member.setName(newName);
        memberRepository.save(member);
    }

}
