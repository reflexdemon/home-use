package io.vpv.homeuse.homeuse.service;

import io.vpv.homeuse.homeuse.dao.AuditLogDao;
import io.vpv.homeuse.homeuse.model.OAuth2Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuditService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuditLogDao auditLogDao;

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
}
