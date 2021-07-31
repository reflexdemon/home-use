package io.vpv.homeuse.controller.api;

import io.vpv.homeuse.model.OAuth2Log;
import io.vpv.homeuse.model.User;
import io.vpv.homeuse.service.AuditService;
import io.vpv.homeuse.service.UserSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;

@RestController
public class UserAPI {

    final
    UserSession session;

    final
    AuditService auditService;

    public UserAPI(UserSession session, AuditService auditService) {
        this.session = session;
        this.auditService = auditService;
    }


    @GetMapping("/user")
    public User userFromSession() {
        return session.getValueFromSession(LOGGED_IN_USER, User.class);
    }

    @GetMapping("/user/log")
    public Flux<OAuth2Log> userLog() {
        return auditService.getAuditLog();
    }

}
