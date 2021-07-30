package io.vpv.homeuse.homeuse.controller.page;

import io.vpv.homeuse.homeuse.model.User;
import io.vpv.homeuse.homeuse.service.HoneywellService;
import io.vpv.homeuse.homeuse.service.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static io.vpv.homeuse.homeuse.config.security.Constants.LOGGED_IN_USER;

@Controller
@RequestMapping({"/link"})
public class AccountLinkController {

    @Autowired
    UserSession session;

    @Autowired
    HoneywellService honeywellService;

    @GetMapping
    public String authorize(Model model, @RequestParam String provider) {
        User user = session.getValueFromSession(LOGGED_IN_USER, User.class);
        String redirectLink = honeywellService.getAuthorizeLink(user);
        return "redirect:" + redirectLink;
    }
}
