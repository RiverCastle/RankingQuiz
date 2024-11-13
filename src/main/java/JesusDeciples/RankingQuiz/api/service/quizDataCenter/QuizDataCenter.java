package JesusDeciples.RankingQuiz.api.service.quizDataCenter;

import JesusDeciples.RankingQuiz.api.dto.AnswerDto;
import JesusDeciples.RankingQuiz.api.dto.QuizContentDto;
import JesusDeciples.RankingQuiz.api.dto.QuizDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.facade.QuizQuizContentFacade;
import JesusDeciples.RankingQuiz.api.facade.QuizScoreFacade;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.DataCenterState;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.state.WAITING;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class QuizDataCenter {
    @Getter @Setter
    private Quiz presentQuiz;
    @Getter @Setter
    private DataCenterState presentState = new WAITING();
    private final Long waitingTime = 3L; // 퀴즈 수거 대기 시간
    private final QuizScoreFacade quizScoreFacade;
    private final QuizQuizContentFacade quizQuizContentFacade;
    private final Map<String, AnswerDto> savedAnswerDtos = new HashMap<>();
    @Getter
    private final Map<String, QuizResultDto> results = new HashMap<>();
    @Getter
    private String winnerName;

    public void handle() {
        this.presentState.handle(this);
    }

    private QuizResultDto getQuizResult(String sessionId) {
        if (savedAnswerDtos.containsKey(sessionId)) {
            AnswerDto answerDto = savedAnswerDtos.get(sessionId);
            return quizScoreFacade.score(presentQuiz.getId(), answerDto);
        }
        return null;
    }

    public void loadAnswerFromUser(String sessionId, AnswerDto answerDto) {
        savedAnswerDtos.put(sessionId, answerDto);
    }

    private void clearAnswers() {
        savedAnswerDtos.clear();
    }
    private void clearResults() {
        results.clear();
    }

    public LocalDateTime getPresentQuizFinishedAt() {
        return this.presentQuiz.getFinishedAt();
    }

    public void setNewQuizExcept() {
        this.presentQuiz = // 기존 퀴즈 Content를 제외한 Quiz Content로 새 퀴즈 생성 및 할당
                quizQuizContentFacade.setNewQuizExcept(presentQuiz.getQuizContent().getId());
        clearResults();
        clearAnswers();
    }

    public void score() {
        clearWinnerName();
        clearResults(); // 채점 시작 전 채점 결과 Collection clear
        Set<String> quizParticipantsSessionIds = savedAnswerDtos.keySet();
        LocalDateTime fastest = LocalDateTime.MAX;
        for (String sessionId : quizParticipantsSessionIds) {
            QuizResultDto resultDto = getQuizResult(sessionId);
            if (resultDto.isCorrect() & fastest.isAfter(resultDto.getWrittenAt())) {
                fastest = resultDto.getWrittenAt();
                this.winnerName = resultDto.getUserName();
            }
            results.put(sessionId, resultDto);
        }
        clearAnswers(); // 채점 후 정답 Collection clear
    }

    public void initiateQuiz() {
        this.presentQuiz = quizQuizContentFacade.setNewQuiz();
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
}
