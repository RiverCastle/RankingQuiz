package JesusDeciples.RankingQuiz.api.service.quizReport;

import JesusDeciples.RankingQuiz.api.dto.QuizReportDto;
import JesusDeciples.RankingQuiz.api.entity.QuizReport;
import JesusDeciples.RankingQuiz.api.repository.QuizReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizReportServiceImpl implements QuizReportService {

    private final QuizReportRepository quizReportRepository;

    @Override
    public void save(QuizReportDto dto) {
        quizReportRepository.save(QuizReport.from(dto));
    }
}
