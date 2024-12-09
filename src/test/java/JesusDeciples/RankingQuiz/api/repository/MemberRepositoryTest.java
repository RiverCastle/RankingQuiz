package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.Member;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class MemberRepositoryTest {
    @Autowired
    private MemberRepository repository;

    @Test
    @DisplayName("Email Null Member Save Test")
    void testSaveMemberEntity() {
        Member testEntity = new Member();
        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(testEntity);
        });
    }
}