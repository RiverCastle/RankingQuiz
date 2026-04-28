package JesusDeciples.RankingQuiz.api.admin.service;

import JesusDeciples.RankingQuiz.api.entity.QuizSubjectStatus;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.repository.QuizSubjectStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizStatusService {

    private final QuizSubjectStatusRepository repository;

    @Transactional(readOnly = true)
    public Map<QuizCategory, Boolean> getAllStatus() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(QuizSubjectStatus::getCategory, QuizSubjectStatus::isEnabled));
    }

    @Transactional(readOnly = true)
    public boolean getStatus(QuizCategory category) {
        return repository.findById(category)
                .map(QuizSubjectStatus::isEnabled)
                .orElse(false);
    }

    @Transactional
    public void setStatus(QuizCategory category, boolean enabled) {
        QuizSubjectStatus status = repository.findById(category)
                .orElse(new QuizSubjectStatus(category, enabled));
        status.setEnabled(enabled);
        repository.save(status);
    }
}
