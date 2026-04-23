package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.repository.MultipleChoiceQuizContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MultipleChoiceQuizContentServiceImpl implements MultipleChoiceQuizContentService {
    private final MultipleChoiceQuizContentRepository repository;
    @Override
    public MultipleChoiceQuizContent createMultipleChoiceQuizContent(QuizContentCreateDto dto) {
        List<String> multipleOptions = dto.getMultipleOptions();
        if (multipleOptions == null) throw new RuntimeException("보기가 필요합니다. 보기를 추가해주세요.");
        else if (multipleOptions.size() <= 1) throw new RuntimeException("보기의 항목 수가 너무 적습니다. 현재 개수 = " + multipleOptions.size());
        return MultipleChoiceQuizContent.of(dto);
    }

    @Override
    public void saveToRepository(MultipleChoiceQuizContent entity) {
        repository.save(entity);
    }
}
