package JesusDeciples.RankingQuiz.api.webSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomTextMessageFactory {
    private final ObjectMapper objectMapper;
    public String produceTextMessage(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
