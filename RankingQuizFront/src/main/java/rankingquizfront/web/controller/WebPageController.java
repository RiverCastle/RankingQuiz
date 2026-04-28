package rankingquizfront.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class WebPageController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${backend.api.base-url}")
    private String backendApiUrl;

    @GetMapping("/")
    public String home() {
        return "home/home";
    }

    @GetMapping("/quiz/voca")
    public String quizVoca() {
        if (!isQuizEnabled("ENG_VOCA")) return "redirect:/?quizDisabled=true";
        return "quiz/voca/voca";
    }

    @GetMapping("/quiz/bible")
    public String quizBible() {
        if (!isQuizEnabled("BIBLE")) return "redirect:/?quizDisabled=true";
        return "quiz/bible/bible";
    }

    @SuppressWarnings("unchecked")
    private boolean isQuizEnabled(String category) {
        try {
            Map<String, Boolean> status = restTemplate.getForObject(
                    backendApiUrl + "/quiz-status/public", Map.class);
            return status != null && Boolean.TRUE.equals(status.get(category));
        } catch (Exception e) {
            return true;
        }
    }
}
