package JesusDeciples.RankingQuiz.api.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;

    @PrePersist  // 영속화 이전에 생성시각 할당
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
    public void setFinishedAt(Integer seconds) { // 생성시각 + 사용자 요청 시각 = 퀴즈 종료시각
        this.finishedAt = this.createdAt.plusSeconds(seconds);
    }
}
