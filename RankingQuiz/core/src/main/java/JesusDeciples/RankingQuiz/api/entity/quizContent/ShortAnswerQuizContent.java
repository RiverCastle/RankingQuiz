package JesusDeciples.RankingQuiz.api.entity.quizContent;

import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class ShortAnswerQuizContent extends QuizContent {
    @Id
    @GeneratedValue
    private Long id;

    public static ShortAnswerQuizContent of(QuizContentCreateDto dto) {
        if (dto.getStatement() == null) throw new RuntimeException("문제가 없습니다.");
        if (dto.getAnswer() == null) throw new RuntimeException("정답이 없습니다.");
        if (dto.getCategoryCode() == null) throw new RuntimeException("카테고리를 설정해주세요.");

        ShortAnswerQuizContent entity = new ShortAnswerQuizContent();
        entity.setStatement(dto.getStatement());
        entity.setAnswer(dto.getAnswer());
        entity.setTimeLimit(dto.getTimeLimit());
        entity.setCategoryCode(dto.getCategoryCode());
        return entity;
    }
}
