package io.vpv.homeuse.util;


/******************************************************************************
 * Copyright 2021 reflexdemon                                                 *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software"), *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS    *
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,*
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL    *
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING    *
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER        *
 * DEALINGS IN THE SOFTWARE.                                                  *
 ******************************************************************************/

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
