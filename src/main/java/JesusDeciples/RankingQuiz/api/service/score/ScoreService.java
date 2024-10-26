package JesusDeciples.RankingQuiz.api.service.score;

import org.springframework.stereotype.Service;

@Service
public interface ScoreService {
    void score(Long quizId, String answer);
}
