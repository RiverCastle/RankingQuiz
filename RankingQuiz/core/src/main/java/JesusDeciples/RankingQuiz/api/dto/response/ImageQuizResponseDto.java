package JesusDeciples.RankingQuiz.api.dto.response;

import JesusDeciples.RankingQuiz.api.entity.quizContent.ImageQuizContent;
import lombok.Data;

import java.util.List;

@Data
public class ImageQuizResponseDto {
    private Long id;
    private String type;
    private String category;
    private String question;
    private String imageUrl;
    private String answer;
    private List<String> choices;

    public static ImageQuizResponseDto fromEntity(ImageQuizContent entity) {
        ImageQuizResponseDto dto = new ImageQuizResponseDto();
        dto.setId(entity.getId());
        dto.setType("IMAGE");
        dto.setCategory(entity.getCategory() != null ? entity.getCategory().name() : null);
        dto.setQuestion(entity.getStatement());
        dto.setImageUrl(entity.getImageUrl());
        dto.setAnswer(entity.getAnswer());
        dto.setChoices(entity.getOptions());
        return dto;
    }
}
