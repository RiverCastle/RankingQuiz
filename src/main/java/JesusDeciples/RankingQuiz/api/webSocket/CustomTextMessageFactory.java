package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.MessageWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomTextMessageFactory {
    private final ObjectMapper objectMapper;
    @Transactional
    public String produceTextMessage(Object object) throws JsonProcessingException {
        MessageWrapper messageWrapper = new MessageWrapper();
        messageWrapper.setObject(object);
        messageWrapper.setDataType(object.getClass().getSimpleName());
        return objectMapper.writeValueAsString(messageWrapper);
    }
}
