package rankingquizfront.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
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

    @GetMapping("/quiz/{categoryCode}")
    public String quiz(@PathVariable String categoryCode, Model model) {
        String upperCode = categoryCode.toUpperCase();
        CategoryInfo info = findEnabledCategory(upperCode);
        if (info == null) return "redirect:/?quizDisabled=true";
        model.addAttribute("categoryCode", upperCode);
        model.addAttribute("displayName", info.displayName());
        return "quiz/quiz";
    }

    @SuppressWarnings("unchecked")
    private CategoryInfo findEnabledCategory(String code) {
        try {
            Map<?, ?>[] categories = restTemplate.getForObject(
                    backendApiUrl + "/categories", Map[].class);
            if (categories == null) return null;
            return Arrays.stream(categories)
                    .filter(c -> code.equals(c.get("code")) && Boolean.TRUE.equals(c.get("enabled")))
                    .findFirst()
                    .map(c -> new CategoryInfo((String) c.get("displayName")))
                    .orElse(null);
        } catch (Exception e) {
            return new CategoryInfo(code);
        }
    }

    private record CategoryInfo(String displayName) {}
}
