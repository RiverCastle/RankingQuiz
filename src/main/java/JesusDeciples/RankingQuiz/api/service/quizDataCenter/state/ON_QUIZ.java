package JesusDeciples.RankingQuiz.api.service.quizDataCenter.state;

import JesusDeciples.RankingQuiz.api.webSocket.QuizDataCenter;

import java.time.LocalDateTime;

public class ON_QUIZ implements DataCenterState {
    private LocalDateTime deadline;
    @Override
    public void handle(QuizDataCenter quizDataCenter) {
        if (!(quizDataCenter.getPresentState() instanceof ON_QUIZ)) {
            quizDataCenter.setPresentState(this);
        }
        if (deadline == null) {
            deadline = quizDataCenter.getPresentQuizFinishedAt().minusSeconds(2);
        }
        if (LocalDateTime.now().isAfter(deadline)) {
            quizDataCenter.setPresentState(new COMPLETE_QUIZ());
        }
    }
}