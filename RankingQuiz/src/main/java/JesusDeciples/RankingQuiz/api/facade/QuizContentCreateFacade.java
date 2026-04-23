package JesusDeciples.RankingQuiz.api.facade;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ReferenceTag;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ShortAnswerQuizContent;
import JesusDeciples.RankingQuiz.api.service.MultipleChoiceQuizContentService;
import JesusDeciples.RankingQuiz.api.service.QuizContentLinkReferenceTagService;
import JesusDeciples.RankingQuiz.api.service.ReferenceTagService;
import JesusDeciples.RankingQuiz.api.service.ShortAnswerQuizContentService;
import JesusDeciples.RankingQuiz.api.service.quizContent.QuizContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class QuizContentCreateFacade {
    private final QuizContentService quizContentService;
    private final MultipleChoiceQuizContentService multipleChoiceQuizContentService;
    private final ShortAnswerQuizContentService shortAnswerQuizContentService;
    private final QuizContentLinkReferenceTagService linkService;
    private final ReferenceTagService tagService;

    @Async
    @Transactional
    public void createQuizContent(QuizContentCreateDto dto) {
        QuizType quizType = dto.getQuizType();
        Set<String> tagSet = new HashSet<>(dto.getTags());
        List<ReferenceTag> tags = tagService.getOrCreateTags(tagSet);
        switch (quizType) {
            case MULTIPLE_CHOICE -> {
                MultipleChoiceQuizContent entity = multipleChoiceQuizContentService.createMultipleChoiceQuizContent(dto);
                entity.linkWithTags(linkService.createLinkList(tags));
                multipleChoiceQuizContentService.saveToRepository(entity);
                quizContentService.saveToRepository(entity);
            }
            case SHORT_ANSWER_WRITING -> {
                ShortAnswerQuizContent entity = shortAnswerQuizContentService.createShortAnswerQuizContent(dto);
                entity.linkWithTags(linkService.createLinkList(tags));
                shortAnswerQuizContentService.saveToRepository(entity);
                quizContentService.saveToRepository(entity);
            }
        }
    }
}
