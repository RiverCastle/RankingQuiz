package rankingquizfront.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @GetMapping
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("/login")
    public String adminLogin() {
        return "admin/admin-login";
    }
}
