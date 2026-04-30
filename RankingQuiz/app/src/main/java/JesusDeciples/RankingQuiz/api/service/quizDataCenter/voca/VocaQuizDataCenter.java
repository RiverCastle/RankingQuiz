package JesusDeciples.RankingQuiz.api.service.quizDataCenter.voca;

import JesusDeciples.RankingQuiz.api.dto.AnswerDto;
import JesusDeciples.RankingQuiz.api.dto.QuizContentDto;
import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.facade.QuizQuizContentFacade;
import JesusDeciples.RankingQuiz.api.facade.QuizScoreFacade;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.QuizDataCenter;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.DataCenterState;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.WAITING;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@RequiredArgsConstructor
public class VocaQuizDataCenter extends QuizDataCenter {
    private final QuizCategory category = QuizCategory.ENG_VOCA;
    @Getter @Setter
    private Quiz presentQuiz;
    @Getter @Setter
    private DataCenterState presentState = new WAITING();
    private final Long waitingTime = 3L; // 퀴즈 수거 대기 시간
    private final QuizScoreFacade quizScoreFacade;
    private final QuizQuizContentFacade quizQuizContentFacade;
    @Getter
    private final Queue<AnswerDto> answerQueue = new ConcurrentLinkedQueue<>();
    @Getter
    private final Map<String, QuizResultDto> results = new ConcurrentHashMap<>();
    @Getter
    private String winnerName;
    // [Fix] Long → String: memberId(인증) 또는 sessionId(비회원) 모두 수용
    private final Map<String, Boolean> haveAnswered = new ConcurrentHashMap<>();

    public void handle() {
        this.presentState.handle(this);
    }

    public void loadAnswerFromUser(AnswerDto answerDto) {
        // [Fix] 비회원(memberId=null)은 ConcurrentHashMap이 null key를 허용하지 않으므로 sessionId로 대체
        String dedupeKey = answerDto.getMemberId() != null
                ? answerDto.getMemberId().toString()
                : answerDto.getSessionId();
        if (haveAnswered.putIfAbsent(dedupeKey, true) == null) {
            answerQueue.add(answerDto);
        }
    }

    private void clearAnswers() {
        answerQueue.clear();
        haveAnswered.clear();
    }
    private void clearResults() {
        results.clear();
    }

    public LocalDateTime getPresentQuizFinishedAt() {
        return this.presentQuiz.getFinishedAt();
    }

    public void setNewQuizExcept() {
        setPresentQuiz(quizQuizContentFacade.setNewQuizExcept(presentQuiz.getQuizContent().getId(), category));
        // 기존 퀴즈 Content를 제외한 Quiz Content로 새 퀴즈 생성 및 할당
        clearResults();
        clearAnswers();
    }

    public void score() {
        clearWinnerName();
        clearResults(); // 채점 시작 전 채점 결과 Collection clear
        List<String> winners = new ArrayList<>();
        LocalDateTime fastest = LocalDateTime.MAX;
        for (AnswerDto answerDto : answerQueue) {
            QuizResultDto resultDto = quizScoreFacade.score(presentQuiz.getId(), answerDto);
            results.put(answerDto.getSessionId(), resultDto);
            LocalDateTime writtenAt = resultDto.getWrittenAt();
            if (resultDto.isCorrect()) {
                if (writtenAt.isBefore(fastest)) {
                    winners.clear();
                    fastest = writtenAt;
                    winners.add(resultDto.getUserName());
                } else if (fastest.equals(writtenAt)) {
                    winners.add(resultDto.getUserName());
                }
            }

            winnerName = winners.isEmpty() ? null : String.join(" ", winners);
        }
        clearAnswers();
    }

    public void initiateQuiz() {
        setPresentQuiz(quizQuizContentFacade.setNewQuiz(category));
    }

    public QuizDto getPresentQuizDto() {
        QuizDto dto = new QuizDto();
        dto.setQuizId(presentQuiz.getId());
        dto.setQuizContentDto(new QuizContentDto().fromEntity(presentQuiz.getQuizContent()));
        dto.setStatement(presentQuiz.getQuizContent().getStatement());
        dto.setFinishedAt(presentQuiz.getFinishedAt());
        return dto;
    }
    private void clearWinnerName() {
        this.winnerName = null;
    }

    public void clearDataCenter() {
        this.presentQuiz = null;
        this.presentState = new WAITING();
        clearAnswers();
        clearResults();
        clearWinnerName();
    }

    @Override
    public QuizCategory getCategory() {
        return QuizCategory.ENG_VOCA;
    }
}
