package io.vpv.homeuse.controller.page;

import io.vpv.homeuse.service.HoneywellService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static io.vpv.homeuse.util.SessionUtil.getUserFromSession;
import static io.vpv.homeuse.util.SessionUtil.setUserToSession;

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
    public Mono<String> authorize(ServerWebExchange serverWebExchange,
                                  @RequestParam String code,
                                  @RequestParam String state,
                                  @RequestParam String scope) {
        return getUserFromSession(serverWebExchange)
                .flatMap(user -> honeywellService.getAuthToken(user, code, state, scope))
                .flatMap(u -> setUserToSession(serverWebExchange, u))
                .thenReturn("redirect:/");
    }
}
