package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.QuizDataCenter;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.bible.BibleQuizDataCenter;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.DataCenterState;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.voca.VocaQuizDataCenter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class QuizDataCenterMediator {
    private final Map<QuizCategory, QuizDataCenter> quizDataCenters;

    public QuizDataCenterMediator(VocaQuizDataCenter vocaQuizDataCenter,
                                  BibleQuizDataCenter bibleQuizDataCenter) {
        quizDataCenters = new HashMap<>();
        quizDataCenters.put(QuizCategory.ENG_VOCA, vocaQuizDataCenter);
        quizDataCenters.put(QuizCategory.BIBLE, bibleQuizDataCenter);
    }

    public DataCenterState getQuizDataCenterState(QuizCategory category) {
        return quizDataCenters.get(category).getPresentState();
    }

    public void updateDataCenterStateAndAction(QuizCategory category, DataCenterState dataCenterState) {
        QuizDataCenter quizDataCenter = quizDataCenters.get(category);
        quizDataCenter.setPresentState(dataCenterState);
        quizDataCenter.handle();
    }

    public Map<String, QuizResultDto> getQuizResults(QuizCategory category) {
        return quizDataCenters.get(category).getResults();
    }

    public QuizDto getPresentQuizDto(QuizCategory category) {
        return quizDataCenters.get(category).getPresentQuizDto();
    }

    public String getQuizWinnerName(QuizCategory category) {
        return quizDataCenters.get(category).getWinnerName();
    }
}
