package JesusDeciples.RankingQuiz.api.service.quiz;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.response.MultipleChoiceQuizResponseDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResponseDto;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.repository.QuizRepository;
import JesusDeciples.RankingQuiz.api.service.MultipleChoiceQuizContentFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final MultipleChoiceQuizContentFactory multipleChoiceQuizContentFactory;
    private final QuizRepository quizRepository;
    @Override
    @Transactional
    public QuizResponseDto addNewQuiz(QuizType quizType) {
        if (quizType == QuizType.MULTIPLE_CHOICE) { // 객관식 퀴즈 생성
            MultipleChoiceQuizContent quizContent = multipleChoiceQuizContentFactory.makeMultipleChoiceQuizContent();
            Quiz entity = new Quiz(quizContent);
            entity = quizRepository.save(entity);

            MultipleChoiceQuizResponseDto dto = new MultipleChoiceQuizResponseDto(); // 클라이언트에게 보낼 DTO
            dto.id = entity.getId();
            dto.setOptions(quizContent.getOptions());
            dto.setStatement(quizContent.getStatement());
            dto.timeLimit = quizContent.getTimeLimit();
            return dto;
        }
        return null;
    }

    @Override
    public Quiz getQuiz(Long quizId) {
        return quizRepository.findByIdWithMultipleChoiceQuizContent(quizId).orElse(null);
    }

    @Override
    public Quiz addNewQuiz(QuizContent quizContent) {
        Quiz entitiy = new Quiz();
        entitiy.setQuizContent(quizContent);
        entitiy.setCreatedAt();
        entitiy.setFinishedAt(quizContent.getTimeLimit());
        return quizRepository.save(entitiy);
    }
}
