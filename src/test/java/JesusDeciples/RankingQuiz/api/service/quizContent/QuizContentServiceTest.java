package JesusDeciples.RankingQuiz.api.service.quizContent;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ShortAnswerQuizContent;
import JesusDeciples.RankingQuiz.api.repository.MultipleChoiceQuizContentRepository;
import JesusDeciples.RankingQuiz.api.repository.QuizContentRepository;
import JesusDeciples.RankingQuiz.api.repository.ShortAnswerQuizContentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizContentServiceTest {
    private QuizContentService quizContentService;
    @Mock
    private QuizContentRepository quizContentRepository;
    @Mock
    private MultipleChoiceQuizContentRepository multipleChoiceQuizContentRepository;
    @Mock
    private ShortAnswerQuizContentRepository shortAnswerQuizContentRepository;

    @BeforeEach
    public void setUp() {
        quizContentService = new QuizContentServiceImpl(multipleChoiceQuizContentRepository, quizContentRepository, shortAnswerQuizContentRepository);
    }

    @Test
    @DisplayName("퀴즈 컨텐츠 타입 미명시 테스트")
    void testAddQuizContent1() {
        QuizContentCreateDto createDto = new QuizContentCreateDto();
        quizContentService.addQuizContent(createDto);

        verify(multipleChoiceQuizContentRepository, times(0)).save(any(MultipleChoiceQuizContent.class));
        verify(shortAnswerQuizContentRepository, times(0)).save(any(ShortAnswerQuizContent.class));
        verify(quizContentRepository, times(0)).save(any(MultipleChoiceQuizContent.class));
    }

    @Test
    @DisplayName("보기가 없는 객관식 퀴즈 컨텐츠 테스트")
    void testAddQuizContent2() {
        QuizContentCreateDto createDto = new QuizContentCreateDto();
        createDto.setQuizType(QuizType.MULTIPLE_CHOICE);
        quizContentService.addQuizContent(createDto);

        verify(multipleChoiceQuizContentRepository, times(0)).save(any(MultipleChoiceQuizContent.class));
        verify(quizContentRepository, times(0)).save(any(MultipleChoiceQuizContent.class));
    }

}