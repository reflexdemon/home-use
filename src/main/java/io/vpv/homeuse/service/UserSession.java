package io.vpv.homeuse.service;

import io.vpv.homeuse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;
import static java.util.Objects.nonNull;
import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

@Service
@Scope(value = SCOPE_SESSION, proxyMode = TARGET_CLASS)
public class UserSession {

    @Autowired
    HttpSession httpSession;

    /**
     * If the datatype does not match then only NULL is returned
     *
     * @param sessionKey
     * @param valuetype
     * @param <T>
     * @return
     */
    public <T> T getValueFromSession(final String sessionKey, Class<T> valuetype) {
        Object value = httpSession.getAttribute(sessionKey);
        if (nonNull(value)) {
            return (T) value;
        }

        return null;
    }

    public void updateUser(final User user) {
        httpSession.setAttribute(LOGGED_IN_USER, user);
    }
}
