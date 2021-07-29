package io.vpv.homeuse.homeuse.controller.page;

import io.vpv.homeuse.homeuse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

import static io.vpv.homeuse.homeuse.config.security.Constants.LOGGED_IN_USER;

@Controller
@RequestMapping({"/", "/index"})
public class IndexController {

    @Autowired
    HttpSession httpSession;

    @GetMapping
    public String main(Model model) {
        User user = (User) httpSession.getAttribute(LOGGED_IN_USER);
        model.addAttribute(LOGGED_IN_USER, user);
        return "index";
    }
}
