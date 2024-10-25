package JesusDeciples.RankingQuiz.api.webSocket;

import JesusDeciples.RankingQuiz.api.dto.MessageWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomTextMessageFactory {
    private final ObjectMapper objectMapper;
    public String produceTextMessage(Object object) throws JsonProcessingException {
        MessageWrapper messageWrapper = new MessageWrapper();
        messageWrapper.setMessageObject(object);
        messageWrapper.setDataType(object.getClass().getName());
        return objectMapper.writeValueAsString(messageWrapper);
    }
}
