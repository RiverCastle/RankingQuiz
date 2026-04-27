package JesusDeciples.RankingQuiz.api.admin.service;

import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuizStatusService {

    private final Map<QuizCategory, Boolean> statusMap = new ConcurrentHashMap<>(Map.of(
            QuizCategory.ENG_VOCA, true,
            QuizCategory.BIBLE, true
    ));

    public Map<QuizCategory, Boolean> getAllStatus() {
        return Map.copyOf(statusMap);
    }

    public boolean getStatus(QuizCategory category) {
        return statusMap.getOrDefault(category, false);
    }

    public void setStatus(QuizCategory category, boolean enabled) {
        statusMap.put(category, enabled);
    }
}
