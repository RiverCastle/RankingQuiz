package JesusDeciples.RankingQuiz.api.service.quizDataCenter.state;

import JesusDeciples.RankingQuiz.api.service.quizDataCenter.VocaQuizDataCenter;

import java.time.LocalDateTime;

public class COMPLETE_QUIZ implements DataCenterState {
    private LocalDateTime deadline;

    @Override
    public void handle(VocaQuizDataCenter vocaQuizDataCenter) {
        if (deadline == null) {
            deadline = vocaQuizDataCenter.getPresentQuizFinishedAt().plusSeconds(2);
        }
        if (LocalDateTime.now().isAfter(deadline))
            vocaQuizDataCenter.setPresentState(new INIT_SCORE());
    }
}
