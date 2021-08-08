package io.vpv.homeuse.controller.page;

import io.vpv.homeuse.model.User;
import io.vpv.homeuse.service.HoneywellService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;

@Controller
@RequestMapping({"/link"})
public class AccountLinkController {

    final
    HoneywellService honeywellService;

    public AccountLinkController(HoneywellService honeywellService) {
        this.honeywellService = honeywellService;
    }

    @GetMapping
    public String authorize(Model model,
                            ServerWebExchange serverWebExchange,
                            @RequestParam String provider) {
        User user = serverWebExchange.getAttribute(LOGGED_IN_USER);
        String redirectLink = honeywellService.getAuthorizeLink(user);
        return "redirect:" + redirectLink;
    }
}
