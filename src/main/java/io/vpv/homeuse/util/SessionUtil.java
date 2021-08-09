package io.vpv.homeuse.util;

import io.vpv.homeuse.model.User;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;

public final class SessionUtil {
    public static Mono<User> getUserFromSession(ServerWebExchange serverWebExchange) {
        return serverWebExchange
                .getSession()
                .mapNotNull(
                        session -> session.getAttribute(LOGGED_IN_USER)
                );
    }

    public static Mono<User> setUserToSession(ServerWebExchange serverWebExchange, User u) {
        return serverWebExchange
                .getSession()
                .mapNotNull(
                        session -> session.getAttributes().put(LOGGED_IN_USER, u)
                ).thenReturn(u);
    }

    public static Mono<Void> removeAttributeFromSession(ServerWebExchange serverWebExchange, String attrName) {
        return serverWebExchange
                .getSession()
                .mapNotNull(
                        session -> session.getAttributes().remove(attrName)
                ).then();

    }
}
