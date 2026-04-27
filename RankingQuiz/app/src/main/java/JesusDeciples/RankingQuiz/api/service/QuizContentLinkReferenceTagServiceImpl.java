package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContentLinkReferenceTag;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ReferenceTag;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizContentLinkReferenceTagServiceImpl implements QuizContentLinkReferenceTagService {
    @Override
    public List<QuizContentLinkReferenceTag> createLinkList(List<ReferenceTag> tags) {
        List<QuizContentLinkReferenceTag> linkList = new ArrayList<>();
        for (ReferenceTag tag : tags) {
            linkList.add(new QuizContentLinkReferenceTag(tag));
        }
        return linkList;
    }
}
