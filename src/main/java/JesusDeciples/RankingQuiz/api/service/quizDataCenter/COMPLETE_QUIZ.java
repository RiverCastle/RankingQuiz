package JesusDeciples.RankingQuiz.api.service.quizDataCenter;

import JesusDeciples.RankingQuiz.api.webSocket.QuizDataCenter;

import java.time.LocalDateTime;

public class COMPLETE_QUIZ implements DataCenterState {
    private LocalDateTime deadline;

    @Override
    public void handle(QuizDataCenter quizDataCenter) {
        if (deadline == null) {
            deadline = quizDataCenter.getPresentQuizFinishedAt().plusSeconds(2);
        }
        if (LocalDateTime.now().isAfter(deadline))
            quizDataCenter.setPresentState(new INIT_SCORE());
    }
}
