package JesusDeciples.RankingQuiz.api.entity.quizContent;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class MultipleChoiceQuizContent extends QuizContent {
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> options;

    public static MultipleChoiceQuizContent of(QuizContentCreateDto dto) {
        if (dto.getStatement() == null) throw new RuntimeException("문제가 없습니다.");
        if (dto.getAnswer() == null) throw new RuntimeException("정답이 없습니다.");
        if (dto.getCategory() == null) throw new RuntimeException("카테고리를 설정해주세요.");
        if (!dto.getMultipleOptions().contains(dto.getAnswer())) throw new RuntimeException("정답이 포함되어 있지 않습니다.");

        MultipleChoiceQuizContent entity = new MultipleChoiceQuizContent();
        entity.setOptions((dto).getMultipleOptions());
        entity.setStatement(dto.getStatement());
        entity.setAnswer(dto.getAnswer());
        entity.setTimeLimit(dto.getTimeLimit());
        entity.setCategory(dto.getCategory());
        return entity;
    }
}
