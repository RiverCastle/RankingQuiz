package JesusDeciples.RankingQuiz.api.facade;

import JesusDeciples.RankingQuiz.api.dto.AnswerDto;
import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizScoreFacade {
//    추후에 score 결과 save를 위해 facade 적용
    private final QuizService quizService;
    public QuizResultDto score(Long quizId, AnswerDto answerDto) {
        Quiz finishedQuiz = quizService.getQuiz(quizId);
        String answer = answerDto.getUserAnswer();
        boolean isCorrect = finishedQuiz.getQuizContent().getAnswer().equals(answer);
        QuizResultDto dto = new QuizResultDto(isCorrect);
        dto.setMyAnswer(answer);
        dto.setAnswer(finishedQuiz.getQuizContent().getAnswer());
        dto.setStatement(finishedQuiz.getQuizContent().getStatement());
        dto.setWrittenAt(answerDto.getWrittenAt());
        dto.setUserName(answerDto.getUserName());
        // TODO score save logic
        return dto;
    }
}
