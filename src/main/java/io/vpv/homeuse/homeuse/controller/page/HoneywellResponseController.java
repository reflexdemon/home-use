package io.vpv.homeuse.homeuse.controller.page;

import io.vpv.homeuse.homeuse.model.User;
import io.vpv.homeuse.homeuse.service.HoneywellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

import static io.vpv.homeuse.homeuse.config.security.Constants.LOGGED_IN_USER;

@Controller
@RequestMapping({"/honeywell/response/code"})
public class HoneywellResponseController {

    @Autowired
    HttpSession httpSession;

    @Autowired
    HoneywellService honeywellService;

    @GetMapping
    public String authorize(Model model,
                            @RequestParam String code,
                            @RequestParam String state,
                            @RequestParam String scope) {
        User user = (User) httpSession.getAttribute(LOGGED_IN_USER);
        User linkedUser = honeywellService.getAuthToken(user, code, state, scope);
        model.addAttribute("linkedUser", linkedUser);
        return "redirect:/";
    }
}
