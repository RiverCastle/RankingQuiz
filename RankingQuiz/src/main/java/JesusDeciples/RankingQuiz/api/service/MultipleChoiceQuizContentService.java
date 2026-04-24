package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import org.springframework.stereotype.Service;

@Service
public interface MultipleChoiceQuizContentService {
    MultipleChoiceQuizContent createMultipleChoiceQuizContent(QuizContentCreateDto dto);

    void saveToRepository(MultipleChoiceQuizContent entity);
}