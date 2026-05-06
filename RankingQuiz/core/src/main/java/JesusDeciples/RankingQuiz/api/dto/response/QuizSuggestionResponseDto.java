package JesusDeciples.RankingQuiz.api.dto.response;

import JesusDeciples.RankingQuiz.api.entity.QuizSuggestion;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class QuizSuggestionResponseDto {

    private final Long id;
    private final String category;
    private final String statement;
    private final String answer;
    private final List<String> choices;
    private final String status;
    private final LocalDateTime createdAt;

    public QuizSuggestionResponseDto(QuizSuggestion suggestion, List<String> parsedChoices) {
        this.id = suggestion.getId();
        this.category = suggestion.getCategory().name();
        this.statement = suggestion.getStatement();
        this.answer = suggestion.getAnswer();
        this.choices = parsedChoices;
        this.status = suggestion.getStatus().name();
        this.createdAt = suggestion.getCreatedAt();
    }
}
