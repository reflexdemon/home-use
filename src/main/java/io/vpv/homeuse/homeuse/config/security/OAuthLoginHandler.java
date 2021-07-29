package io.vpv.homeuse.homeuse.config.security;

import io.vpv.homeuse.homeuse.model.OAuth2Log;
import io.vpv.homeuse.homeuse.model.User;
import io.vpv.homeuse.homeuse.service.AuditService;
import io.vpv.homeuse.homeuse.service.UserService;
import io.vpv.homeuse.homeuse.util.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static io.vpv.homeuse.homeuse.config.security.Constants.*;

@Component("VPVAuthenticationSuccessHandler")
public class OAuthLoginHandler extends AuditService
        implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OAuthUtils oAuthUtils;


    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("Got Authentication Success Response: {}", authentication);
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        Map userAttrMap = oAuthUtils.getUserAttributes(oAuth2AuthenticationToken);
        try {
            String provider = (String) userAttrMap.get(PROVIDER_ID);
            User currentUser = buildUserFromOAuth(userAttrMap);
            User user = getUserFromDB(currentUser);
            //Creates a new HTTP Session
            request.getSession(true).setAttribute(LOGGED_IN_USER, user);
        } catch (Exception e) {
            logger.error("Unable to get the user", e);
        }

        Mono<OAuth2Log> logMono = saveBlockingLog(request, authentication, userAttrMap, LOGIN_SUCCESS);
        logger.info("Response:{}", userAttrMap);
        String requestURL = getRedirectURL(request);
        //Consume
        logMono.block();
        response.sendRedirect(requestURL);
    }

    /**
     * This could potentially create user if it is not already found in the DB
     * @param user
     * @return
     */
    private User getUserFromDB(User user) {
        if (null != user.getId()) {
            Optional<User> u = userService.findById(user.getId()).blockOptional();
            if (u.isPresent()) {
                return u.get();
            }
            return saveUser(user);
        } else if (null != user.getUsername()) {
            Optional<User> uu = userService.findByUsername(user.getUsername()).blockOptional();
            if (uu.isPresent()) {
                return uu.get();
            }
            return saveUser(user);
        } else if ( null != user.getEmail()) {
            User ue = userService.findByEmail(user.getEmail()).blockFirst();
            if (null != ue) {
                return ue;
            }
            return saveUser(user);
        } else {
            return saveUser(user);
        }
    }

    private User saveUser(User user) {
        return userService.save(user).block();
    }

    private String getRedirectURL(HttpServletRequest request) {
        Assert.notNull(request, "Request cannot be null");
        HttpSession session = request.getSession(false);
        Assert.notNull(session, "There needs to be a valid Session");

        String requestURL = "/";
        Object savedRequest = session.getAttribute(SPRING_SECURITY_SAVED_REQUEST);
        if (savedRequest instanceof DefaultSavedRequest) {
            DefaultSavedRequest defaultSavedRequest = (DefaultSavedRequest) savedRequest;
            requestURL = defaultSavedRequest.getRedirectUrl();
        }
        return requestURL;
    }

    public User buildUserFromOAuth(Map userAttrMap) {
        User user = new User();
        user.setId(getAsString(userAttrMap.get("id")));
        user.setUsername(getAsString(userAttrMap.get("login")));
        String name = getAsString(userAttrMap.get("name"));
        user.setUsername(getAsString(userAttrMap.get("sub")));
        if (null != name && name.indexOf(' ') > 1) {
            user.setFirstName(name.substring(0, name.indexOf(' ')));
            user.setLastName(name.substring(name.indexOf(' ') + 1));
        }

        user.setLastName(getAsString(userAttrMap.get("family_name")));
        user.setProfileUrl(getAsString(userAttrMap.get("html_url")));
        user.setSource(getAsString(userAttrMap.get("PROVIDER_ID")));
        user.setAvatarUrl(getAsString(userAttrMap.get("avatar_url")));
        user.setEmail(getAsString(userAttrMap.get("email")));
        user.setUserAttrMap(userAttrMap);
        return user;
    }

    protected String getAsString(Object obj) {
        if (null != obj) {
            return obj.toString();
        }
        return null;
    }

}
