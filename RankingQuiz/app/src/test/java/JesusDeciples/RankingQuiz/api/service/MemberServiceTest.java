package JesusDeciples.RankingQuiz.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import JesusDeciples.RankingQuiz.api.entity.Member;
import JesusDeciples.RankingQuiz.api.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        memberService = new MemberServiceImpl(memberRepository);
    }
    @Test
    @DisplayName("퀴즈 포인트 적립 테스트")
    public void updatePointTest() {

        Member testMember = new Member();
        int prePoint = 123;
        int point = 10;
        long id = 5642L;
        testMember.setPoint(prePoint);
        testMember.setId(id);

        when(memberRepository.save(testMember)).thenReturn(testMember);
        when(memberRepository.findByIdForUpdate(id)).thenReturn(Optional.of(testMember));

        testMember = memberRepository.save(testMember);

        memberService.updatePoint(id, point);
        assertEquals(prePoint + point, testMember.getPoint());
    }
}
