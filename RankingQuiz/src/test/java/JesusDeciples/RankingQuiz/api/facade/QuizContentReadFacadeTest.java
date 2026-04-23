package JesusDeciples.RankingQuiz.api.facade;

import JesusDeciples.RankingQuiz.api.dto.response.QuizContentReviewDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.service.MultipleChoiceQuizContentService;
import JesusDeciples.RankingQuiz.api.service.QuizContentLinkReferenceTagService;
import JesusDeciples.RankingQuiz.api.service.ReferenceTagService;
import JesusDeciples.RankingQuiz.api.service.ShortAnswerQuizContentService;
import JesusDeciples.RankingQuiz.api.service.quizContent.QuizContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizContentReadFacadeTest {
    QuizContentReadFacade facade;
    @Mock
    private QuizContentService quizContentService;
    @Mock
    private MultipleChoiceQuizContentService multipleChoiceQuizContentService;
    @Mock
    private ShortAnswerQuizContentService shortAnswerQuizContentService;
    @Mock
    private QuizContentLinkReferenceTagService linkService;
    @Mock
    private ReferenceTagService tagService;

    @BeforeEach
    void setup() {
        facade = new QuizContentReadFacade(quizContentService);
    }

    @Test
    @DisplayName("Tag가 없는 QuizContent 테스트")
    public void testNonTaggedQuizContentRead() {
        Long quizId = 1L;
        QuizContent quizContentNotTagged = new QuizContent();
        quizContentNotTagged.setId(quizId);

        when(quizContentService.getQuizContentById(quizId)).thenReturn(quizContentNotTagged);

        List<QuizContentReviewDto> result = facade.readQuizContentLinkedWith(quizId);
        List<QuizContentReviewDto> expected = new ArrayList<>();

        expected.add(QuizContentReviewDto.of(quizContentNotTagged));
        assertEquals(expected, result);
    }
}