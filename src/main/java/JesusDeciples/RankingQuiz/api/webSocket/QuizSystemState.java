package JesusDeciples.RankingQuiz.api.webSocket;

public enum QuizSystemState {
    ON_QUIZ_SETTING,
    COMPLETED_QUIZ_SETTING,
    ON_QUIZ,
    COMPLETED_QUIZ_GETTING_ANSWERED,
    ON_SCORING,
    COMPLETED_SCORING,
    WAITING
}
