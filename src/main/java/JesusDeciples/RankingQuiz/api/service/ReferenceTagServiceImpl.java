package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.quizContent.ReferenceTag;
import JesusDeciples.RankingQuiz.api.repository.ReferenceTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReferenceTagServiceImpl implements ReferenceTagService {
    private final ReferenceTagRepository repository;
    @Override
    public List<ReferenceTag> getOrCreateTags(Set<String> tagSet) {
        List<ReferenceTag> existingTags = repository.findAllByTagIn(tagSet);
        Map<String, ReferenceTag> existingTagMap = new HashMap<>();
        for (ReferenceTag tag : existingTags) existingTagMap.put(tag.getTag(), tag);

        List<ReferenceTag> newTags = new ArrayList<>();
        for (String tag : tagSet) {
            if (!existingTagMap.containsKey(tag)) newTags.add(new ReferenceTag(tag));
        }
        newTags = repository.saveAll(newTags);
        existingTags.addAll(newTags);
        return existingTags;
    }
}
