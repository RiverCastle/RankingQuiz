package JesusDeciples.RankingQuiz.api.service.quizDataCenter;

import JesusDeciples.RankingQuiz.api.dto.AnswerDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.facade.QuizQuizContentFacade;
import JesusDeciples.RankingQuiz.api.facade.QuizScoreFacade;
import JesusDeciples.RankingQuiz.api.service.quizDataCenter.voca.VocaQuizDataCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class QuizDataCenterTest {
    private VocaQuizDataCenter quizDataCenter;
    @Mock
    QuizScoreFacade quizScoreFacade;
    @Mock
    QuizQuizContentFacade quizQuizContentFacade;

    @BeforeEach
    void setUp() {
        quizDataCenter = new VocaQuizDataCenter(quizScoreFacade, quizQuizContentFacade);
    }

    @Test
    @DisplayName("여러 유저가 가장 빠르게 동시에 정답을 제출한 경우")
    void testScore() {
        LocalDateTime now = LocalDateTime.now();
        Long quizId = 111L;

        String session1 = "Session1";
        AnswerDto answer1 = new AnswerDto();
        answer1.setSessionId(session1);
        answer1.setQuizId(quizId);
        String session2 = "Session2";
        AnswerDto answer2 = new AnswerDto();
        answer2.setSessionId(session2);
        answer2.setQuizId(quizId);
        String session3 = "Session3";
        AnswerDto answer3 = new AnswerDto();
        answer3.setSessionId(session3);
        answer3.setQuizId(quizId);

        answer1.setMemberId(1L);
        answer2.setMemberId(2L);
        answer3.setMemberId(3L);

        quizDataCenter.loadAnswerFromUser(answer1);
        quizDataCenter.loadAnswerFromUser(answer2);
        quizDataCenter.loadAnswerFromUser(answer3);

        QuizResultDto result1 = new QuizResultDto();
        result1.setWrittenAt(now);
        result1.setCorrect(true);
        result1.setUserName("Answer1");

        QuizResultDto result2 = new QuizResultDto();
        result2.setWrittenAt(now);
        result2.setCorrect(true);
        result2.setUserName("Answer2");

        QuizResultDto result3 = new QuizResultDto();
        result3.setWrittenAt(now);
        result3.setCorrect(true);
        result3.setUserName("Answer3");

        Map<String, QuizResultDto> results = new HashMap<>();
        when(quizScoreFacade.score(quizId, answer1)).thenReturn(result1);
        when(quizScoreFacade.score(quizId, answer2)).thenReturn(result2);
        when(quizScoreFacade.score(quizId, answer3)).thenReturn(result3);

        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        quizDataCenter.setPresentQuiz(quiz);
        quizDataCenter.score();

        assertEquals("Answer1 Answer2 Answer3", quizDataCenter.getWinnerName());
    }

    @Test
    @DisplayName("유저 1명이 Winner")
    void testScoring1() {
        LocalDateTime now = LocalDateTime.now();
        Long quizId = 111L;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        String session1 = "Session1";
        AnswerDto answer1 = new AnswerDto();
        answer1.setSessionId(session1);
        answer1.setQuizId(quizId);
        String session2 = "Session2";
        AnswerDto answer2 = new AnswerDto();
        answer2.setSessionId(session2);
        answer2.setQuizId(quizId);
        String session3 = "Session3";
        AnswerDto answer3 = new AnswerDto();
        answer3.setSessionId(session3);
        answer3.setQuizId(quizId);

        answer1.setMemberId(1L);
        answer2.setMemberId(2L);
        answer3.setMemberId(3L);

        quizDataCenter.loadAnswerFromUser(answer1);
        quizDataCenter.loadAnswerFromUser(answer2);
        quizDataCenter.loadAnswerFromUser(answer3);

        QuizResultDto result1 = new QuizResultDto();
        result1.setWrittenAt(now);
        result1.setCorrect(false);
        result1.setUserName("Answer1");

        QuizResultDto result2 = new QuizResultDto();
        result2.setWrittenAt(now);
        result2.setCorrect(true);
        result2.setUserName("Answer2");

        QuizResultDto result3 = new QuizResultDto();
        result3.setWrittenAt(now);
        result3.setCorrect(false);
        result3.setUserName("Answer3");

        Map<String, QuizResultDto> results = new HashMap<>();
        when(quizScoreFacade.score(quizId, answer1)).thenReturn(result1);
        when(quizScoreFacade.score(quizId, answer2)).thenReturn(result2);
        when(quizScoreFacade.score(quizId, answer3)).thenReturn(result3);

        quizDataCenter.setPresentQuiz(quiz);
        quizDataCenter.score();

        assertEquals("Answer2", quizDataCenter.getWinnerName());
    }

    @Test
    @DisplayName("유저 0명이 Winner")
    void testScoring2() {
        LocalDateTime now = LocalDateTime.now();
        Long quizId = 111L;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        String session1 = "Session1";
        AnswerDto answer1 = new AnswerDto();
        answer1.setSessionId(session1);
        answer1.setQuizId(quizId);
        String session2 = "Session2";
        AnswerDto answer2 = new AnswerDto();
        answer2.setSessionId(session2);
        answer2.setQuizId(quizId);
        String session3 = "Session3";
        AnswerDto answer3 = new AnswerDto();
        answer3.setSessionId(session3);
        answer3.setQuizId(quizId);

        answer1.setMemberId(1L);
        answer2.setMemberId(2L);
        answer3.setMemberId(3L);

        quizDataCenter.loadAnswerFromUser(answer1);
        quizDataCenter.loadAnswerFromUser(answer2);
        quizDataCenter.loadAnswerFromUser(answer3);

        QuizResultDto result1 = new QuizResultDto();
        result1.setWrittenAt(now);
        result1.setCorrect(false);
        result1.setUserName("Answer1");

        QuizResultDto result2 = new QuizResultDto();
        result2.setWrittenAt(now);
        result2.setCorrect(false);
        result2.setUserName("Answer2");

        QuizResultDto result3 = new QuizResultDto();
        result3.setWrittenAt(now);
        result3.setCorrect(false);
        result3.setUserName("Answer3");

        Map<String, QuizResultDto> results = new HashMap<>();
        when(quizScoreFacade.score(quizId, answer1)).thenReturn(result1);
        when(quizScoreFacade.score(quizId, answer2)).thenReturn(result2);
        when(quizScoreFacade.score(quizId, answer3)).thenReturn(result3);

        quizDataCenter.setPresentQuiz(quiz);
        quizDataCenter.score();

        assertEquals(null, quizDataCenter.getWinnerName());
    }

    @Test
    @DisplayName("동일한 사용자가 여러 번 답안을 제출된 경우")
    void testLoadAnswer() {
        Long memberId1 = 1L;
        Long memberId2 = 2L;

        AnswerDto answerSheet1 = new AnswerDto();
        answerSheet1.setMemberId(memberId1);
        AnswerDto answerSheet2 = new AnswerDto();
        answerSheet2.setMemberId(memberId1);
        AnswerDto answerSheet3 = new AnswerDto();
        answerSheet3.setMemberId(memberId2);

        quizDataCenter.loadAnswerFromUser(answerSheet1);
        quizDataCenter.loadAnswerFromUser(answerSheet2);
        quizDataCenter.loadAnswerFromUser(answerSheet3);

        assertEquals(2, quizDataCenter.getAnswerQueue().size());
    }
}