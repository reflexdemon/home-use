package io.vpv.homeuse.controller.api;

import io.vpv.homeuse.model.OAuth2Log;
import io.vpv.homeuse.model.User;
import io.vpv.homeuse.service.AuditService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;

@RestController
public class UserAPI {
    final
    AuditService auditService;

    public UserAPI(AuditService auditService) {
        this.auditService = auditService;
    }


    @GetMapping("/user")
    public Mono<User> userFromSession(ServerWebExchange serverWebExchange) {
        return serverWebExchange
                .getSession()
                .mapNotNull(
                        session -> session.getAttribute(LOGGED_IN_USER)
                );
    }

    @GetMapping("/user/log")
    public Flux<OAuth2Log> userLog(ServerWebExchange serverWebExchange) {
        Mono<User> user = userFromSession(serverWebExchange);
        return auditService.getAuditLog(user);

    }

}
