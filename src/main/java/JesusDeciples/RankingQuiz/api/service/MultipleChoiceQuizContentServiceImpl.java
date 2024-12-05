package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.repository.MultipleChoiceQuizContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MultipleChoiceQuizContentServiceImpl implements MultipleChoiceQuizContentService {
    private final MultipleChoiceQuizContentRepository repository;
    @Override
    public MultipleChoiceQuizContent createMultipleChoiceQuizContent(QuizContentCreateDto dto) {
        return null;
    }

    @Override
    public void saveToRepository(MultipleChoiceQuizContent entity) {

    }
}
