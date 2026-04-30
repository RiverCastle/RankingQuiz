package JesusDeciples.RankingQuiz.api.admin.service;

import JesusDeciples.RankingQuiz.api.entity.QuizCategory;
import JesusDeciples.RankingQuiz.api.repository.QuizCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizCategoryService {

    private final QuizCategoryRepository repository;

    @Transactional(readOnly = true)
    public List<QuizCategory> getAllCategories() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<QuizCategory> getEnabledCategories() {
        return repository.findAllByEnabledTrue();
    }

    @Transactional(readOnly = true)
    public Map<String, Boolean> getAllStatusMap() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(QuizCategory::getCode, QuizCategory::isEnabled));
    }

    @Transactional(readOnly = true)
    public boolean getStatus(String categoryCode) {
        return repository.findById(categoryCode)
                .map(QuizCategory::isEnabled)
                .orElse(false);
    }

    @Transactional
    public void setStatus(String categoryCode, boolean enabled) {
        QuizCategory cat = repository.findById(categoryCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown category: " + categoryCode));
        cat.setEnabled(enabled);
        repository.save(cat);
    }

    @Transactional
    public QuizCategory createCategory(String code, String displayName, boolean allowMultipleWinners) {
        if (repository.existsById(code)) {
            throw new IllegalArgumentException("Category already exists: " + code);
        }
        return repository.save(QuizCategory.of(code, displayName, true, allowMultipleWinners));
    }

    @Transactional(readOnly = true)
    public QuizCategory getCategory(String code) {
        return repository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("Unknown category: " + code));
    }
}
