package io.vpv.homeuse.controller.api;

import io.vpv.homeuse.model.OAuth2Log;
import io.vpv.homeuse.model.User;
import io.vpv.homeuse.service.AuditService;
import io.vpv.homeuse.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.vpv.homeuse.util.SessionUtil.getUserFromSession;

@RestController
public class UserAPI {
    final
    AuditService auditService;

    final
    UserService userService;

    public UserAPI(AuditService auditService, UserService userService) {
        this.auditService = auditService;
        this.userService = userService;
    }


    @GetMapping("/user")
    public Mono<User> userFromSession(ServerWebExchange serverWebExchange) {
        return getUserFromSession(serverWebExchange);
    }

    @GetMapping("/user/{id}")
    public Mono<User> userById(@PathVariable String id) {
        return userService.findById(id);
    }

    @GetMapping("/user/log")
    public Flux<OAuth2Log> userLog(ServerWebExchange serverWebExchange) {
        Mono<User> user = getUserFromSession(serverWebExchange);
        return auditService.getAuditLog(user);

    }

}
