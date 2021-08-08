package io.vpv.homeuse.service;

import io.vpv.homeuse.dao.AuditLogDao;
import io.vpv.homeuse.model.OAuth2Log;
import io.vpv.homeuse.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

@Service
public class AuditService {

    final AuditLogDao auditLogDao;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuditService(AuditLogDao auditLogDao) {
        this.auditLogDao = auditLogDao;
    }


    protected Mono<OAuth2Log> getoAuth2Log(Authentication authentication, Mono<Map<String, String>> userAttrMap, String action, Map headers) {

        return userAttrMap.map(attr -> OAuth2Log.builder().action(action)
                .attributes(attr)
                .headers(headers)
                .username(authentication.getName())
                .timestamp(new Date())
                .build());
    }

    public Mono<OAuth2Log> saveBlockingLog(WebFilterExchange webFilterExchange, Authentication authentication, Mono<Map<String, String>> userAttrMap, String action) {
        Mono<OAuth2Log> log = getoAuth2Log(authentication, userAttrMap, action, auditLogDao.getHeaders(webFilterExchange));

        return log.map(l -> {
                    logger.info("Saving the value for: {}", l);
                    return l;
                }).
                flatMap(l -> auditLogDao.save(l));
    }

    public Flux<OAuth2Log> getAuditLog(Mono<User> user) {
        Assert.notNull(user, "User cannot be null");
        return user.map(User::getUsername).flatMapMany(auditLogDao::findByUsername);
    }
}
