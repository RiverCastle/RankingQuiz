package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContentLinkReferenceTag;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ReferenceTag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuizContentLinkReferenceTagService {
    List<QuizContentLinkReferenceTag> createLinkList(List<ReferenceTag> tagSet);
}
