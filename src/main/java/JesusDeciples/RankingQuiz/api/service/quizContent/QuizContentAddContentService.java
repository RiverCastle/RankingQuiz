package JesusDeciples.RankingQuiz.api.service.quizContent;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.repository.MultipleChoiceQuizContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizContentAddContentService implements QuizContentService {
    private final MultipleChoiceQuizContentRepository multipleChoiceQuizContentRepository;
    @Override
    public void addQuiz(QuizContentCreateDto dto) {
        if (dto.getQuizType() == QuizType.MULTIPLE_CHOICE) {
            List<String> multipleOptions = dto.getMultipleOptions();
            if (multipleOptions != null) {
                MultipleChoiceQuizContent entity = MultipleChoiceQuizContent.builder()
                        .options((dto).getMultipleOptions()).build();
                entity.setStatement(dto.getStatement());
                entity.setAnswer(dto.getAnswer());
                entity.setTimeLimit(dto.getTimeLimit());
                multipleChoiceQuizContentRepository.save(entity);
            }
        }
    }
}
