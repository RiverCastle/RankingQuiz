package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.quizContent.ImageQuizContent;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.repository.ImageQuizContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageQuizContentService {

    private final ImageQuizContentRepository repository;
    private final FileStorageService fileStorageService;

    @Transactional
    public ImageQuizContent create(String category, String question, String imageUrl, String answer, List<String> choices) {
        if (!choices.contains(answer)) {
            throw new IllegalArgumentException("정답이 보기에 포함되어 있지 않습니다.");
        }
        ImageQuizContent entity = new ImageQuizContent();
        entity.setCategory(QuizCategory.valueOf(category));
        entity.setStatement(question);
        entity.setImageUrl(imageUrl);
        entity.setAnswer(answer);
        entity.setOptions(choices);
        entity.setTimeLimit(10);
        return repository.save(entity);
    }

    @Transactional
    public ImageQuizContent update(Long id, String category, String question, String newImageUrl, String answer, List<String> choices) {
        ImageQuizContent entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지 퀴즈를 찾을 수 없습니다. id=" + id));

        if (!choices.contains(answer)) {
            throw new IllegalArgumentException("정답이 보기에 포함되어 있지 않습니다.");
        }

        if (newImageUrl != null && !newImageUrl.equals(entity.getImageUrl())) {
            fileStorageService.deleteFile(entity.getImageUrl());
            entity.setImageUrl(newImageUrl);
        }

        entity.setCategory(QuizCategory.valueOf(category));
        entity.setStatement(question);
        entity.setAnswer(answer);
        entity.setOptions(choices);
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public ImageQuizContent findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지 퀴즈를 찾을 수 없습니다. id=" + id));
    }
}
