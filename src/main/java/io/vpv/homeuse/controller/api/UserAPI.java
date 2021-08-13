package io.vpv.homeuse.controller.api;


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
