package io.vpv.homeuse.controller.page;

import io.vpv.homeuse.model.User;
import io.vpv.homeuse.service.HoneywellService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static io.vpv.homeuse.util.SessionUtil.getUserFromSession;

@Controller
@RequestMapping({"/link"})
public class AccountLinkController {

    final
    HoneywellService honeywellService;

    public AccountLinkController(HoneywellService honeywellService) {
        this.honeywellService = honeywellService;
    }

    @GetMapping
    public Mono<String> authorize(Model model,
                                  ServerWebExchange serverWebExchange,
                                  @RequestParam String provider) {
        Mono<User> user = getUserFromSession(serverWebExchange);
        return honeywellService.getAuthorizeLink(user);
    }
}
