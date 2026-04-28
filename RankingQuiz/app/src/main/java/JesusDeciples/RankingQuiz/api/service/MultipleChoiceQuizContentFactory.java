package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.repository.MultipleChoiceQuizContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class MultipleChoiceQuizContentFactory {
    private final MultipleChoiceQuizContentRepository multipleChoiceQuizContentRepository;

    public MultipleChoiceQuizContent makeMultipleChoiceQuizContent() {

        long max = multipleChoiceQuizContentRepository.findMaxId();
        long min = multipleChoiceQuizContentRepository.findMinId();
        boolean found = false;
        while (!found) {
            Optional<MultipleChoiceQuizContent> optional = multipleChoiceQuizContentRepository.findByIdWithOptions(new Random().nextLong(min, max));
            if (optional.isPresent()) return optional.get();
        }
        return null;
    }
}
