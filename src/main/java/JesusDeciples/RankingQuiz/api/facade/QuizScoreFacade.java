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
//    추후에 score 결과 save를 위해 facade 적용
    private final QuizService quizService;
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

        return dto;
    }
}
