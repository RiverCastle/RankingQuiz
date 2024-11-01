package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.Member;
import JesusDeciples.RankingQuiz.api.entity.QuizResult;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.repository.QuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuizResultServiceImpl implements QuizResultService {
    private final QuizResultRepository quizResultRepository;
    @Override
    public QuizResult addQuizResult(Quiz quiz, Member member, String userAnswer, LocalDateTime writtenAt) {
        QuizResult quizResult = new QuizResult();
        quizResult.setQuiz(quiz);
        quizResult.setCorrect(quiz.getQuizContent().getAnswer().equals(userAnswer));
        quizResult.setMember(member);
        quizResult.setMyAnswer(userAnswer);
        quizResult.setWrittenAt(writtenAt);
        return quizResultRepository.save(quizResult);
    }
}