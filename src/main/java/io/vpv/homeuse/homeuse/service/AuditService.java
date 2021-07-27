package io.vpv.homeuse.homeuse.service;

import io.vpv.homeuse.homeuse.dao.AuditLogDao;
import io.vpv.homeuse.homeuse.model.OAuth2Log;
import io.vpv.homeuse.homeuse.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.vpv.homeuse.homeuse.config.security.Constants.LOGGED_IN_USER;

public class AuditService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuditLogDao auditLogDao;

    @Autowired private HttpSession session;
    protected OAuth2Log getoAuth2Log(Authentication authentication, Map userAttrMap, String action, Map headers) {
        OAuth2Log log = new OAuth2Log();
        log.setAction(action);
        log.setAttributes(userAttrMap);
        log.setHeaders(headers);
        log.setUsername(authentication.getName());
        return log;
    }

    protected Map getHeaders(final HttpServletRequest request) {
        final Map<String, String> map = new HashMap<>();
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> {
                    //Just remember HTTP header can have duplicate keys
                    String oldValue = map.get(headerName);
                    if (null == oldValue) {
                        map.put(headerName, request.getHeader(headerName));
                    } else {
                        //We have already got this header
                        map.put(headerName, oldValue + "; " + request.getHeader(headerName));
                    }
                });
        return map;
    }

    protected Mono<OAuth2Log> saveBlockingLog(HttpServletRequest request, Authentication authentication, Map userAttrMap, String action) {
        OAuth2Log log = getoAuth2Log(authentication, userAttrMap, action, getHeaders(request));
        logger.info("Saving the value for: {}", log);
        Mono<OAuth2Log> logMono = auditLogDao.save(log);
        return logMono;
    }

    public Flux<OAuth2Log> getAuditLog() {
        User user = (User) session.getAttribute(LOGGED_IN_USER);
        if (null != user) {
         return auditLogDao.findByUsername(user.getUsername());
        }

        return Flux.empty();
    }
}
