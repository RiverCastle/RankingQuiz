package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.Member;
import JesusDeciples.RankingQuiz.api.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Rollback
@SpringBootTest
class MemberServiceIntegrationTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    private Member testMember;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @BeforeEach
    void setUp() {
        memberService = new MemberServiceImpl(memberRepository);
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setName("test");
        member.setPoint(new Random().nextInt(100) + 1);
        testMember = memberRepository.save(member);
    }

    @Test
    @DisplayName("Member Point 적립 동시성 테스트")
    void testUpdatePoint() throws InterruptedException {
        Long memberId = testMember.getId();
        int point = testMember.getPoint();
        System.out.println("현재 점수  = " + point);
        int pointToAdd = new Random().nextInt(10) + 1;
        System.out.println("적립될 점수 = " + pointToAdd);
        int threadNumber = 32;
        System.out.println("    x    ");
        System.out.println("적립 횟수   = " + threadNumber);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(threadNumber);

        for (int t = 0; t < threadNumber; t++) {
            executorService.submit(() -> {
                try {
                    transactionTemplate.execute(status -> {
                        memberService.updatePoint(memberId, pointToAdd);
                        return null;
                    });
                } catch (Exception e) {
                    System.out.println("e = " + e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Member member = memberService.getMemberById(memberId);
        int expected = point + pointToAdd * threadNumber;
        int actual = member.getPoint();
        System.out.println("-----결과-----");
        System.out.println("expected = " + expected);
        System.out.println("actual   = " + actual);
        assertEquals(expected, actual);
    }
}