package io.vpv.homeuse.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;
import static io.vpv.homeuse.util.SessionUtil.removeAttributeFromSession;

@Component("VPVLogoutHandler")
public class OAuthLogoutHandler implements ServerLogoutHandler {
    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
        return removeAttributeFromSession(exchange.getExchange(), LOGGED_IN_USER)
                .then();
    }
}
