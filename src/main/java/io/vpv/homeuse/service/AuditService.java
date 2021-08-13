package io.vpv.homeuse.service;


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

import io.vpv.homeuse.dao.AuditLogDao;
import io.vpv.homeuse.model.OAuth2Log;
import io.vpv.homeuse.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

import static io.vpv.homeuse.util.SessionUtil.getUserFromSession;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

@Service
public class AuditService {

    final AuditLogDao auditLogDao;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuditService(AuditLogDao auditLogDao) {
        this.auditLogDao = auditLogDao;
    }


    protected Mono<OAuth2Log> getoAuth2Log(Authentication authentication, Mono<Map<String, String>> userAttrMap, String action, Map<String, String> headers) {

        return userAttrMap.map(attr -> OAuth2Log.builder().action(action)
                .attributes(attr)
                .headers(headers)
                .username(authentication.getName())
                .timestamp(new Date())
                .build());
    }

    public Mono<OAuth2Log> saveLog(ServerWebExchange serverWebExchange, Map<String, String> actionMap, String action) {
        return getUserFromSession(serverWebExchange)
                .flatMap(
                        user -> saveLog(user, actionMap, action)
                );
    }

    public Mono<OAuth2Log> saveLog(User user, Map<String, String> actionMap, String action) {
        return auditLogDao.save(OAuth2Log.builder()
                .action(action)
                .attributes(actionMap)
                .username(user.getUsername())
                .timestamp(new Date())
                .build());
    }

    public Mono<OAuth2Log> saveLog(ServerWebExchange serverWebExchange, Authentication authentication, Mono<Map<String, String>> userAttrMap, String action) {
        Mono<OAuth2Log> log = getoAuth2Log(authentication, userAttrMap, action, getHeaders(serverWebExchange));

        return log.map(l -> {
                    logger.info("Saving the value for: {}", l);
                    return l;
                })
                .flatMap(l -> auditLogDao.save(l));
    }

    public Flux<OAuth2Log> getAuditLog(Mono<User> user) {
        Assert.notNull(user, "User cannot be null");
        return user.map(User::getUsername).flatMapMany(auditLogDao::findByUsername);
    }

    protected Map<String, String> getHeaders(ServerWebExchange serverWebExchange) {
        return serverWebExchange
                .getRequest()
                .getHeaders()
                .entrySet()
                .stream()
                .map(e -> entry(e.getKey(), String.join(";", e.getValue())))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

    }
}
