package io.vpv.homeuse.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;
import static io.vpv.homeuse.util.SessionUtil.getUserFromSession;

@Controller
@RequestMapping({"/", "/index", "index.html"})
public class IndexController {
    @GetMapping
    public Mono<String> index(Model model, ServerWebExchange serverWebExchange) {


        return getUserFromSession(serverWebExchange)
                .map(u -> model.addAttribute(LOGGED_IN_USER, u)).thenReturn("index");
    }
}
