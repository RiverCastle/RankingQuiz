package JesusDeciples.RankingQuiz.api.service.quizDataCenter;

import JesusDeciples.RankingQuiz.api.dto.AnswerDto;
import JesusDeciples.RankingQuiz.api.dto.QuizContentDto;
import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.facade.QuizQuizContentFacade;
import JesusDeciples.RankingQuiz.api.facade.QuizScoreFacade;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.DataCenterState;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.WAITING;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GenericQuizDataCenter extends QuizDataCenter {

    private final String categoryCode;
    private final boolean allowMultipleWinners;
    private final QuizScoreFacade quizScoreFacade;
    private final QuizQuizContentFacade quizQuizContentFacade;

    @Getter @Setter
    private Quiz presentQuiz;
    @Getter @Setter
    private DataCenterState presentState = new WAITING();
    @Getter
    private String winnerName;
    @Getter
    private final Map<String, QuizResultDto> results = new ConcurrentHashMap<>();

    @Getter
    private final Queue<AnswerDto> answerQueue = new ConcurrentLinkedQueue<>();
    private final Map<String, Boolean> haveAnswered = new ConcurrentHashMap<>();

    public GenericQuizDataCenter(String categoryCode,
                                 boolean allowMultipleWinners,
                                 QuizScoreFacade quizScoreFacade,
                                 QuizQuizContentFacade quizQuizContentFacade) {
        this.categoryCode = categoryCode;
        this.allowMultipleWinners = allowMultipleWinners;
        this.quizScoreFacade = quizScoreFacade;
        this.quizQuizContentFacade = quizQuizContentFacade;
    }

    @Override
    public String getCategoryCode() {
        return categoryCode;
    }

    @Override
    public void handle() {
        this.presentState.handle(this);
    }

    @Override
    public void loadAnswerFromUser(AnswerDto answerDto) {
        String dedupeKey = answerDto.getMemberId() != null
                ? answerDto.getMemberId().toString()
                : answerDto.getSessionId();
        if (haveAnswered.putIfAbsent(dedupeKey, true) == null) {
            answerQueue.add(answerDto);
        }
    }

    @Override
    public void score() {
        winnerName = null;
        results.clear();
        if (allowMultipleWinners) {
            scoreMultipleWinners();
        } else {
            scoreSingleWinner();
        }
        answerQueue.clear();
        haveAnswered.clear();
    }

    private void scoreSingleWinner() {
        for (AnswerDto dto : answerQueue) {
            QuizResultDto result = quizScoreFacade.score(presentQuiz.getId(), dto);
            if (winnerName == null && result.isCorrect()) {
                winnerName = result.getUserName();
            }
            results.put(dto.getSessionId(), result);
        }
    }

    private void scoreMultipleWinners() {
        List<String> winners = new ArrayList<>();
        LocalDateTime fastest = LocalDateTime.MAX;
        for (AnswerDto dto : answerQueue) {
            QuizResultDto result = quizScoreFacade.score(presentQuiz.getId(), dto);
            results.put(dto.getSessionId(), result);
            if (result.isCorrect()) {
                LocalDateTime writtenAt = result.getWrittenAt();
                if (writtenAt.isBefore(fastest)) {
                    winners.clear();
                    fastest = writtenAt;
                    winners.add(result.getUserName());
                } else if (fastest.equals(writtenAt)) {
                    winners.add(result.getUserName());
                }
            }
        }
        winnerName = winners.isEmpty() ? null : String.join(" ", winners);
    }

    @Override
    public LocalDateTime getPresentQuizFinishedAt() {
        return presentQuiz.getFinishedAt();
    }

    @Override
    public void initiateQuiz() {
        presentQuiz = quizQuizContentFacade.setNewQuiz(categoryCode);
    }

    @Override
    public void setNewQuizExcept() {
        presentQuiz = quizQuizContentFacade.setNewQuizExcept(presentQuiz.getQuizContent().getId(), categoryCode);
        results.clear();
        answerQueue.clear();
        haveAnswered.clear();
    }

    @Override
    public QuizDto getPresentQuizDto() {
        QuizDto dto = new QuizDto();
        dto.setQuizId(presentQuiz.getId());
        dto.setQuizContentDto(new QuizContentDto().fromEntity(presentQuiz.getQuizContent()));
        dto.setStatement(presentQuiz.getQuizContent().getStatement());
        dto.setFinishedAt(presentQuiz.getFinishedAt());
        return dto;
    }

    @Override
    public void clearDataCenter() {
        presentQuiz = null;
        presentState = new WAITING();
        answerQueue.clear();
        haveAnswered.clear();
        results.clear();
        winnerName = null;
    }

    @Override
    public QuizCategory getCategory() {
        return QuizCategory.valueOf(categoryCode);
    }
}
