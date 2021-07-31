package io.vpv.homeuse.controller.page;

import io.vpv.homeuse.model.User;
import io.vpv.homeuse.service.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;

@Controller
@RequestMapping({"/", "/index"})
public class IndexController {

    final UserSession session;

    public IndexController(UserSession userSession) {
        this.session = userSession;
    }

    @GetMapping
    public String main(Model model) {
        User user = session.getValueFromSession(LOGGED_IN_USER, User.class);
        model.addAttribute(LOGGED_IN_USER, user);
        return "index";
    }
}
