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

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;

@Controller
@RequestMapping({"/honeywell/response/code"})
public class HoneywellResponseController {
    //


    final
    HoneywellService honeywellService;

    public HoneywellResponseController(HoneywellService honeywellService) {
        this.honeywellService = honeywellService;
    }

    @GetMapping
    public String authorize(Model model,
                            ServerWebExchange serverWebExchange,
                            @RequestParam String code,
                            @RequestParam String state,
                            @RequestParam String scope) {
        User user = serverWebExchange.getAttribute(LOGGED_IN_USER);
        Mono<User> linkedUser = honeywellService.getAuthToken(user, code, state, scope)
                .map(u -> {
                    serverWebExchange.getAttributes().put(LOGGED_IN_USER, u);
                    return u;
                });
        
        model.addAttribute("linkedUser", linkedUser);
        return "redirect:/";
    }
}
