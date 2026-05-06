package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.dto.request.QuizSuggestionCreateDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizSuggestionResponseDto;
import JesusDeciples.RankingQuiz.api.entity.QuizSuggestion;
import JesusDeciples.RankingQuiz.api.enums.SuggestionStatus;
import JesusDeciples.RankingQuiz.api.facade.QuizContentCreateFacade;
import JesusDeciples.RankingQuiz.api.repository.QuizSuggestionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizSuggestionService {

    private final QuizSuggestionRepository repository;
    private final QuizContentCreateFacade createFacade;
    private final ObjectMapper objectMapper;

    @Transactional
    public void submitSuggestion(QuizSuggestionCreateDto dto) {
        if (!dto.getChoices().contains(dto.getAnswer())) {
            throw new IllegalArgumentException("정답이 보기 목록에 포함되어 있지 않습니다.");
        }
        if (dto.getChoices().size() < 2) {
            throw new IllegalArgumentException("보기는 최소 2개 이상이어야 합니다.");
        }
        String choicesJson;
        try {
            choicesJson = objectMapper.writeValueAsString(dto.getChoices());
        } catch (Exception e) {
            throw new IllegalArgumentException("보기 데이터 처리 중 오류가 발생했습니다.");
        }
        QuizSuggestion suggestion = QuizSuggestion.of(dto.getCategory(), dto.getStatement(), dto.getAnswer(), choicesJson);
        repository.save(suggestion);
    }

    public List<QuizSuggestionResponseDto> getPendingSuggestions() {
        return repository.findByStatusOrderByCreatedAtDesc(SuggestionStatus.PENDING)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional
    public void approveSuggestion(Long id) {
        QuizSuggestion suggestion = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("제안을 찾을 수 없습니다."));

        List<String> choices;
        try {
            choices = objectMapper.readValue(suggestion.getChoices(), new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalStateException("보기 데이터 파싱 중 오류가 발생했습니다.");
        }

        QuizContentCreateDto createDto = new QuizContentCreateDto();
        createDto.setStatement(suggestion.getStatement());
        createDto.setAnswer(suggestion.getAnswer());
        createDto.setCategory(suggestion.getCategory());
        createDto.setQuizType(QuizType.MULTIPLE_CHOICE);
        createDto.setMultipleOptions(choices);
        createDto.setTags(Collections.emptyList());

        createFacade.createQuizContent(createDto);
        suggestion.approve();
    }

    @Transactional
    public void rejectSuggestion(Long id) {
        QuizSuggestion suggestion = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("제안을 찾을 수 없습니다."));
        suggestion.reject();
    }

    private QuizSuggestionResponseDto toResponseDto(QuizSuggestion suggestion) {
        List<String> choices;
        try {
            choices = objectMapper.readValue(suggestion.getChoices(), new TypeReference<>() {});
        } catch (Exception e) {
            choices = Collections.emptyList();
        }
        return new QuizSuggestionResponseDto(suggestion, choices);
    }
}
