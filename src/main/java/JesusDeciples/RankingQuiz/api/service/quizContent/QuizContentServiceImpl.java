package JesusDeciples.RankingQuiz.api.service.quizContent;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ShortAnswerQuizContent;
import JesusDeciples.RankingQuiz.api.repository.MultipleChoiceQuizContentRepository;
import JesusDeciples.RankingQuiz.api.repository.QuizContentRepository;
import JesusDeciples.RankingQuiz.api.repository.ShortAnswerQuizContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static JesusDeciples.RankingQuiz.api.dto.QuizType.SHORT_ANSWER_WRITING;

@Service
@RequiredArgsConstructor
public class QuizContentServiceImpl implements QuizContentService {
    private final MultipleChoiceQuizContentRepository multipleChoiceQuizContentRepository;
    private final QuizContentRepository quizContentRepository;
    private final ShortAnswerQuizContentRepository shortAnswerQuizContentRepository;
    @Override
    public void addQuiz(QuizContentCreateDto dto) {
        QuizType dtoType = dto.getQuizType();
        if (dtoType == QuizType.MULTIPLE_CHOICE) {
            List<String> multipleOptions = dto.getMultipleOptions();
            if (multipleOptions != null) {
                MultipleChoiceQuizContent entity = new MultipleChoiceQuizContent();
                entity.setOptions((dto).getMultipleOptions());
                entity.setStatement(dto.getStatement());
                entity.setAnswer(dto.getAnswer());
                entity.setTimeLimit(dto.getTimeLimit());
                multipleChoiceQuizContentRepository.save(entity);
                quizContentRepository.save(entity);
            }
            // 보기 없는 보기형 문제 생성 요청의 경우는 예외처리

        } else if (dtoType == SHORT_ANSWER_WRITING) {
            ShortAnswerQuizContent entity = new ShortAnswerQuizContent();
            entity.setStatement(dto.getStatement());
            entity.setAnswer(dto.getAnswer());
            entity.setTimeLimit(dto.getTimeLimit());
            shortAnswerQuizContentRepository.save(entity);
            quizContentRepository.save(entity);
        }
    }

    @Override
    public QuizContent getQuizContentExcept(Long presentQuizContentId) {
        long max = quizContentRepository.findMaxId();
        long min = quizContentRepository.findMinId();
        boolean found = false;

        while (!found) {
            Long randomId = new Random().nextLong(min, max);
            if (randomId.equals(presentQuizContentId)) continue;
            Optional<QuizContent> optional = quizContentRepository.findById(randomId);
//            TODO 퀴즈 타입에 따라 Fetch join을 걸어줘야함. 일단 보류
            if (optional.isPresent()) return optional.get();
        }
        return null;
    }

    @Override
    public QuizContent getRandomQuizContent() {
        long max = quizContentRepository.findMaxId();
        long min = quizContentRepository.findMinId();

        boolean found = false;
        int count = 0;
        while (!found) {
            Long randomId = new Random().nextLong(min, max + 1);
            Optional<QuizContent> optional = quizContentRepository.findById(randomId);
            if (!optional.isEmpty()) return optional.get();
            count++;
            if (count > 10) found = true;
        }
        return null;
    }
}

// 새로운 문제의 유형을 추가할 때,
// 새 유형 Entity를 만들고
// 그에 해당하는 Repository를 만들고,
// 여기 클래스에 타입에 따라 저장해주면 된다.
// TODO 꼭 여기에 코드가 추가되어야하나? 다른 곳에 코드를 작성해서 새 유형을 넣을 수 있으면 좋을 듯
