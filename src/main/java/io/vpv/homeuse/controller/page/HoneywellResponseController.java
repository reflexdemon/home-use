package io.vpv.homeuse.controller.page;

import io.vpv.homeuse.model.User;
import io.vpv.homeuse.service.HoneywellService;
import io.vpv.homeuse.service.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;

@Controller
@RequestMapping({"/honeywell/response/code"})
public class HoneywellResponseController {

    @Autowired
    UserSession session;

    @Autowired
    HoneywellService honeywellService;

    @GetMapping
    public String authorize(Model model,
                            @RequestParam String code,
                            @RequestParam String state,
                            @RequestParam String scope) {
        User user = session.getValueFromSession(LOGGED_IN_USER, User.class);
        User linkedUser = honeywellService.getAuthToken(user, code, state, scope).block();
        session.updateUser(linkedUser);
        model.addAttribute("linkedUser", linkedUser);
        return "redirect:/";
    }
}
