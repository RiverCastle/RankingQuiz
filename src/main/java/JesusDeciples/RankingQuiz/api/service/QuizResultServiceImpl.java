package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.response.QuizResultDto;
import JesusDeciples.RankingQuiz.api.entity.Member;
import JesusDeciples.RankingQuiz.api.entity.QuizResult;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.repository.QuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    @Transactional
    public List<QuizResultDto> getMyQuizResults(Long memberId) {
        List<QuizResult> results = quizResultRepository.findAllByMemberId(memberId);
        List<QuizResultDto> resultDtos = new ArrayList<>();
        for (QuizResult result : results) {
            if (result.isCorrect()) continue; // 틀린 문제만 결과 제공
            QuizContent quizContent = result.getQuiz().getQuizContent();
            String answer = quizContent.getAnswer();
            String myAnswer = result.getMyAnswer();
            boolean haveAnswered = true;
            if (myAnswer == null) {
                myAnswer = "없음";
                haveAnswered = true;
            }

            QuizResultDto resultDto = new QuizResultDto();
            resultDto.setId(quizContent.getId());
            resultDto.setCorrect(answer.equals(myAnswer));
            resultDto.setStatement(quizContent.getStatement());
            resultDto.setAnswer(answer);
            resultDto.setMyAnswer(myAnswer);
            resultDto.setHaveAnswered(haveAnswered);
            resultDto.setWrittenAt(result.getWrittenAt());

            resultDtos.add(resultDto);
        }
        return resultDtos;
    }
}