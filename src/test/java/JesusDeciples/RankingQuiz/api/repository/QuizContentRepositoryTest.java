package JesusDeciples.RankingQuiz.api.repository;

import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuizContentRepositoryTest {
    @Autowired
    private QuizContentRepository repository;

    @Test
    @DisplayName("Category별 QuizContent 조회 테스트")
    void testFindRandomByIdNotAndCategory() {
        List<QuizContent> testEntities = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            QuizContent entity = new QuizContent();
            entity.setCategory(QuizCategory.ENG_VOCA);
            testEntities.add(entity);
        }

        for (int i = 0; i < 3; i++) {
            QuizContent entity = new QuizContent();
            entity.setCategory(QuizCategory.BIBLE);
            testEntities.add(entity);
        }

        repository.saveAll(testEntities);

        assertEquals(QuizCategory.BIBLE, repository.findRandomByCategory("BIBLE").get().getCategory());
        assertEquals(QuizCategory.ENG_VOCA, repository.findRandomByCategory("ENG_VOCA").get().getCategory());
    }
}