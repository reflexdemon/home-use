package io.vpv.homeuse.config;

import io.vpv.homeuse.config.security.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("VPVLogoutHandler")
public class OAuthLogoutHandler implements ServerLogoutHandler {
    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
        return exchange.getExchange().getSession()
                .map(webSession -> webSession.getAttributes().remove(Constants.LOGGED_IN_USER))
                .then();
    }
}
