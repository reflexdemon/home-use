package io.vpv.homeuse.homeuse.controller.api;

import io.vpv.homeuse.homeuse.model.OAuth2Log;
import io.vpv.homeuse.homeuse.service.AuditService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
public class UserAPI {


    final AuditService auditService;

    public UserAPI(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes();
    }

    @GetMapping("/user/log")
    public Flux<OAuth2Log> userLog() {
        return auditService.getAuditLog();
    }

}
