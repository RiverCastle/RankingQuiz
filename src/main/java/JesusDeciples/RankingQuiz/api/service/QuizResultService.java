package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.entity.Member;
import JesusDeciples.RankingQuiz.api.entity.QuizResult;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface QuizResultService {
    QuizResult addQuizResult(Quiz quiz, Member member, String userAnswer, LocalDateTime writtenAt);
}
