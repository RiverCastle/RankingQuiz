package JesusDeciples.RankingQuiz.api.entity;

import JesusDeciples.RankingQuiz.api.dto.ServiceFeedbackDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ServiceFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer satisfactionScore;

    @Column(length = 500)
    private String serviceTags;

    @Column(length = 1000)
    private String generalComment;

    private String category;

    private String deviceInfo;

    private LocalDateTime createdAt;

    @PrePersist
    private void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public static ServiceFeedback from(ServiceFeedbackDto dto) {
        ServiceFeedback feedback = new ServiceFeedback();
        feedback.satisfactionScore = dto.getSatisfactionScore();
        feedback.generalComment = dto.getGeneralComment();
        feedback.category = dto.getCategory();
        feedback.deviceInfo = dto.getDeviceInfo();

        List<String> tags = dto.getServiceTags();
        feedback.serviceTags = (tags != null && !tags.isEmpty())
                ? String.join(",", tags)
                : null;

        return feedback;
    }
}
