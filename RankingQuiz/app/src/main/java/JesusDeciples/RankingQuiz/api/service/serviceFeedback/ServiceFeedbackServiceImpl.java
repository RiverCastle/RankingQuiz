package JesusDeciples.RankingQuiz.api.service.serviceFeedback;

import JesusDeciples.RankingQuiz.api.dto.ServiceFeedbackDto;
import JesusDeciples.RankingQuiz.api.entity.ServiceFeedback;
import JesusDeciples.RankingQuiz.api.repository.ServiceFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceFeedbackServiceImpl implements ServiceFeedbackService {

    private final ServiceFeedbackRepository serviceFeedbackRepository;

    @Override
    public void save(ServiceFeedbackDto dto) {
        serviceFeedbackRepository.save(ServiceFeedback.from(dto));
    }
}
