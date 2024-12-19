package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.quizContent.ReferenceTag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ReferenceTagService {
    List<ReferenceTag> getOrCreateTags(Set<String> tagSet);
}
