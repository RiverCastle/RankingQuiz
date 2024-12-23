package JesusDeciples.RankingQuiz.api.facade;

import JesusDeciples.RankingQuiz.api.dto.AnswerDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.entity.Member;
import JesusDeciples.RankingQuiz.api.entity.QuizResult;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.service.MemberService;
import JesusDeciples.RankingQuiz.api.service.QuizResultService;
import JesusDeciples.RankingQuiz.api.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizScoreFacade {
    private final QuizService quizService;
    private final QuizResultService quizResultService;
    private final MemberService memberService;

    public QuizResultDto score(Long quizId, AnswerDto answerDto) {
        Quiz quiz = quizService.getQuiz(quizId);
        String userAnswer = answerDto.getUserAnswer();
        boolean isCorrect = quiz.getQuizContent().getAnswer().equals(userAnswer);

        QuizResultDto dto = new QuizResultDto(isCorrect);
        dto.setMyAnswer(userAnswer);
        dto.setAnswer(quiz.getQuizContent().getAnswer());
        dto.setStatement(quiz.getQuizContent().getStatement());
        dto.setWrittenAt(answerDto.getWrittenAt());
        dto.setUserName(answerDto.getUserName());

        // 인증된 사용자의 경우, 퀴즈 결과 저장
        Long memberId = answerDto.getMemberId();
        if (memberId != null) {
            Member member = memberService.getMemberById(answerDto.getMemberId());
            QuizResult quizResult = quizResultService.addQuizResult(quiz, member, answerDto.getUserAnswer(), answerDto.getWrittenAt());
            if (quizResult.isCorrect()) memberService.updatePoint(memberId, 1);
        }
        return dto;
    }
}
