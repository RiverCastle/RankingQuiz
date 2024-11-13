package JesusDeciples.RankingQuiz.api.service.quizDataCenter.state;

import JesusDeciples.RankingQuiz.api.service.quizDataCenter.VocaQuizDataCenter;

import java.time.LocalDateTime;

public class ON_QUIZ implements DataCenterState {
    private LocalDateTime deadline;
    @Override
    public void handle(VocaQuizDataCenter vocaQuizDataCenter) {
        if (!(vocaQuizDataCenter.getPresentState() instanceof ON_QUIZ)) {
            vocaQuizDataCenter.setPresentState(this);
        }
        if (deadline == null) {
            deadline = vocaQuizDataCenter.getPresentQuizFinishedAt().minusSeconds(2);
        }
        if (LocalDateTime.now().isAfter(deadline)) {
            vocaQuizDataCenter.setPresentState(new COMPLETE_QUIZ());
        }
    }
}