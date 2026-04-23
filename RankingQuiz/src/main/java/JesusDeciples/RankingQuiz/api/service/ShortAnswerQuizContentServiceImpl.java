package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ShortAnswerQuizContent;
import JesusDeciples.RankingQuiz.api.repository.ShortAnswerQuizContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortAnswerQuizContentServiceImpl implements ShortAnswerQuizContentService {
    private final ShortAnswerQuizContentRepository repository;
    @Override
    public ShortAnswerQuizContent createShortAnswerQuizContent(QuizContentCreateDto dto) {
        return ShortAnswerQuizContent.of(dto);
    }

    @Override
    public void saveToRepository(ShortAnswerQuizContent entity) {
        repository.save(entity);
    }
}
