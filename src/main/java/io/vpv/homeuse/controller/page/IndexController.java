package io.vpv.homeuse.controller.page;

import io.vpv.homeuse.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;

@Controller
@RequestMapping({"/", "/index"})
public class IndexController {
    @GetMapping
    public Mono<String> index(Model model, ServerWebExchange serverWebExchange) {


        return serverWebExchange
                .getSession()
                .mapNotNull(
                        session -> session.getAttribute(LOGGED_IN_USER)
                ).cast(User.class)
                .map(u -> model.addAttribute(LOGGED_IN_USER, u)).thenReturn("index");
    }
}
