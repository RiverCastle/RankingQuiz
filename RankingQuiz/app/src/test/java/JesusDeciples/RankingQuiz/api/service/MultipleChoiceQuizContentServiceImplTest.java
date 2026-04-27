package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.repository.MultipleChoiceQuizContentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MultipleChoiceQuizContentServiceImplTest {
    private MultipleChoiceQuizContentService service;
    @Mock
    MultipleChoiceQuizContentRepository repository;
    @BeforeEach
    void setup() {
        service = new MultipleChoiceQuizContentServiceImpl(repository);
    }
    @Test
    void testCreateMultipleChoiceQuizContent() {
        QuizContentCreateDto dto = new QuizContentCreateDto();
        dto.setCategory(QuizCategory.ENG_VOCA);
        assertThrows(RuntimeException.class, () -> {
            service.createMultipleChoiceQuizContent(dto);
        });

        dto.setMultipleOptions(List.of("1"));
        assertThrows(RuntimeException.class, () -> {
            service.createMultipleChoiceQuizContent(dto);
        });

        dto.setStatement("quiestion");
        assertThrows(RuntimeException.class, () -> {
            service.createMultipleChoiceQuizContent(dto);
        });

        dto.setMultipleOptions(List.of("1", "2"));
        assertThrows(RuntimeException.class, () -> {
            service.createMultipleChoiceQuizContent(dto);
        });
        dto.setAnswer("3");
        assertThrows(RuntimeException.class, () -> {
            service.createMultipleChoiceQuizContent(dto);
        });
        dto.setAnswer("1");
        assertDoesNotThrow(() -> {
            service.createMultipleChoiceQuizContent(dto);
        });
    }
}