package rankingquizfront.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "home/home";
    }

    @GetMapping("/quiz/voca")
    public String quizVoca() {
        return "quiz/voca/voca";
    }

    @GetMapping("/quiz/bible")
    public String quizBible() {
        return "quiz/bible/bible";
    }
}
