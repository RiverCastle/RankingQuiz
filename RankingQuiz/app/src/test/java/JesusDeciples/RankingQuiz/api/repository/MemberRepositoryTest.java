package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.Member;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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