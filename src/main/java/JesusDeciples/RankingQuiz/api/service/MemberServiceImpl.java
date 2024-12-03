package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.response.MemberInfoResponseDto;
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

    @Override
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("유저를 조회하지 못했습니다."));
    }

    @Override
    public void updatePoint(Long memberId, int point) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("유저를 조회하지 못했습니다."));
        member.setPoint(member.getPoint() + point);
        memberRepository.save(member);
    }
}
