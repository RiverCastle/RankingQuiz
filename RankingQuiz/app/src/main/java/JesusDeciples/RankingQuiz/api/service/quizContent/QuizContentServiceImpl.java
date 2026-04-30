package JesusDeciples.RankingQuiz.api.service.quizContent;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.*;
import JesusDeciples.RankingQuiz.api.repository.MultipleChoiceQuizContentRepository;
import JesusDeciples.RankingQuiz.api.repository.QuizContentRepository;
import JesusDeciples.RankingQuiz.api.repository.ShortAnswerQuizContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static JesusDeciples.RankingQuiz.api.dto.QuizType.SHORT_ANSWER_WRITING;

@Service
@RequiredArgsConstructor
public class QuizContentServiceImpl implements QuizContentService {
    private final MultipleChoiceQuizContentRepository multipleChoiceQuizContentRepository;
    private final QuizContentRepository repository;
    private final ShortAnswerQuizContentRepository shortAnswerQuizContentRepository;

    @Override
    public void addQuizContent(QuizContentCreateDto dto) {
        QuizType dtoType = dto.getQuizType();
        if (dtoType == QuizType.MULTIPLE_CHOICE) {
            List<String> multipleOptions = dto.getMultipleOptions();
            if (multipleOptions != null) {
                MultipleChoiceQuizContent entity = new MultipleChoiceQuizContent();
                entity.setOptions(dto.getMultipleOptions());
                entity.setStatement(dto.getStatement());
                entity.setAnswer(dto.getAnswer());
                entity.setTimeLimit(dto.getTimeLimit());
                entity.setCategoryCode(dto.getCategoryCode());
                multipleChoiceQuizContentRepository.save(entity);
                repository.save(entity);
            }
        } else if (dtoType == SHORT_ANSWER_WRITING) {
            ShortAnswerQuizContent entity = new ShortAnswerQuizContent();
            entity.setStatement(dto.getStatement());
            entity.setAnswer(dto.getAnswer());
            entity.setTimeLimit(dto.getTimeLimit());
            entity.setCategoryCode(dto.getCategoryCode());
            shortAnswerQuizContentRepository.save(entity);
            repository.save(entity);
        }
    }

    @Override
    public QuizContent getQuizContentExcept(Long presentQuizContentId, String categoryCode) {
        return repository.findRandomByIdNotAndCategory(presentQuizContentId, categoryCode)
                .orElseThrow(() -> new RuntimeException("NOT FOUND"));
    }

    @Override
    public QuizContent getRandomQuizContent(String categoryCode) {
        return repository.findRandomByCategory(categoryCode)
                .orElseThrow(() -> new RuntimeException("NOT FOUND"));
    }

    @Override
    public void saveToRepository(QuizContent entity) {
        repository.save(entity);
    }

    @Override
    public QuizContent getQuizContentById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("NOT FOUND on this id"));
    }

    @Override
    public List<QuizContent> findAllByReferenceTagIn(Set<ReferenceTag> tagSet) {
        return repository.findDistinctAllByTagIn(tagSet);
    }
}
