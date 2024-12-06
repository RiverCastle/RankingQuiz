package JesusDeciples.RankingQuiz.api.facade;

import JesusDeciples.RankingQuiz.api.dto.response.QuizContentReviewDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContentLinkReferenceTag;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ReferenceTag;
import JesusDeciples.RankingQuiz.api.service.MultipleChoiceQuizContentService;
import JesusDeciples.RankingQuiz.api.service.QuizContentLinkReferenceTagService;
import JesusDeciples.RankingQuiz.api.service.ReferenceTagService;
import JesusDeciples.RankingQuiz.api.service.ShortAnswerQuizContentService;
import JesusDeciples.RankingQuiz.api.service.quizContent.QuizContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class QuizContentReadFacade {
    private final QuizContentService quizContentService;
    private final MultipleChoiceQuizContentService multipleChoiceQuizContentService;
    private final ShortAnswerQuizContentService shortAnswerQuizContentService;
    private final QuizContentLinkReferenceTagService linkService;
    private final ReferenceTagService tagService;

    public List<QuizContentReviewDto> readQuizContentLinkedWith(Long quizContentId) {
        QuizContent quizContent = quizContentService.getQuizContentById(quizContentId);
        List<QuizContentLinkReferenceTag> links = quizContent.getLinks();
        Set<QuizContentReviewDto> dtoSet = new HashSet<>();

        if (links.isEmpty()) {
            dtoSet.add(QuizContentReviewDto.of(quizContent));
            return new ArrayList<>(dtoSet);
        }

        Set<ReferenceTag> tagSet = new HashSet<>();
        for (QuizContentLinkReferenceTag link : links) {
            tagSet.add(link.getReferenceTag());
        }

        List<QuizContent> entities = quizContentService.findAllByReferenceTagIn(tagSet);
        for (QuizContent entity : entities) {
            dtoSet.add(QuizContentReviewDto.of(entity));
        }
        return new ArrayList<>(dtoSet);
    }
}
