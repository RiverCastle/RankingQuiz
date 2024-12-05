package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContentLinkReferenceTag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface QuizContentLinkReferenceTagService {
    List<QuizContentLinkReferenceTag> createLinkList(Set<String> tagSet);
}
