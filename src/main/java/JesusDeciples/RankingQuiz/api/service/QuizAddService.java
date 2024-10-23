package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.request.MultipleChoiceQuizCreateDto;
import JesusDeciples.RankingQuiz.api.entity.MultipleChoiceQuiz;
import JesusDeciples.RankingQuiz.api.entity.Quiz;
import JesusDeciples.RankingQuiz.api.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizAddService implements QuizService {
    private final QuizRepository quizRepository;
    @Override
    public void addQuiz(MultipleChoiceQuizCreateDto multipleChoiceQuizCreateDto) {
        Quiz entity = MultipleChoiceQuiz.builder()
                .options(multipleChoiceQuizCreateDto.getOptions()).build();
        entity.setStatement(multipleChoiceQuizCreateDto.getAnswer());
        entity.setStatement(multipleChoiceQuizCreateDto.getStatement());
        entity = quizRepository.save(entity);
        entity.setFinishedAt(multipleChoiceQuizCreateDto.getTimeLimit());
        quizRepository.save(entity);
    }
}
