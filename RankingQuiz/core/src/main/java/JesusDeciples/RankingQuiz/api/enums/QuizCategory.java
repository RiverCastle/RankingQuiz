package JesusDeciples.RankingQuiz.api.enums;

public enum QuizCategory {
    ENG_VOCA("단어 퀴즈"),
    BIBLE("성경 퀴즈");

    private final String displayName;

    QuizCategory(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }
}
