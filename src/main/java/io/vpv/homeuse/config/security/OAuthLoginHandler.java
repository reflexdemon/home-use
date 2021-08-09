package io.vpv.homeuse.config.security;

import io.vpv.homeuse.model.User;
import io.vpv.homeuse.service.AuditService;
import io.vpv.homeuse.service.UserService;
import io.vpv.homeuse.util.OAuthUtils;
import io.vpv.homeuse.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Component("VPVAuthenticationSuccessHandler")
public class OAuthLoginHandler
        extends RedirectServerAuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final OAuthUtils oAuthUtils;


    private final UserService userService;
    private final AuditService auditService;

    public OAuthLoginHandler(OAuthUtils oAuthUtils, UserService userService, AuditService auditService) {
        this.oAuthUtils = oAuthUtils;
        this.userService = userService;
        this.auditService = auditService;
    }


    private Mono<User> getReactiveDBUser(final Mono<User> user) {
        Assert.notNull(user, "User cannot be empty");
        return user
                .flatMap(u -> userService.findById(u.getId()))
                .flatMap(userService::save)
                .map(u -> {
                    logger.info("User from DB is: {}", u);
                    return u;
                });

    }

    private Mono<String> getRedirectURL(WebFilterExchange webFilterExchange) {
        Assert.notNull(webFilterExchange, "Request cannot be null");
        return webFilterExchange.getExchange().getSession()
                .map(WebSession::getAttributes)
                .filter(map -> map.containsKey(Constants.SPRING_SECURITY_SAVED_REQUEST))
                .map(map -> map.get(Constants.SPRING_SECURITY_SAVED_REQUEST))
                .filter(DefaultSavedRequest.class::isInstance)
                .map(DefaultSavedRequest.class::cast)
                .map(DefaultSavedRequest::getRedirectUrl);

    }

    public Mono<User> buildUserFromOAuth(Mono<Map<String, String>> userMap) {
        return userMap
                .map(attr -> {
                    logger.info("Response:{}", attr);
                    return attr;
                })
                .map(
                        userAttrMap -> User.builder()
                                .id(userAttrMap.get("sub"))
                                .username(userAttrMap.get("sub"))
                                .firstName(userAttrMap.get("given_name"))
                                .lastName(userAttrMap.get("family_name"))
                                .profileUrl(userAttrMap.get("html_url"))
                                .source(userAttrMap.get("PROVIDER_ID"))
                                .avatarUrl(userAttrMap.get("picture"))
                                .email(userAttrMap.get("email")).build()
                );
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        logger.info("Got Authentication Success Response: {}", authentication);
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        Mono<Map<String, String>> userAttrMap = oAuthUtils.getUserAttributes(oAuth2AuthenticationToken);

        Mono<User> currentUser = buildUserFromOAuth(userAttrMap);
        Mono<User> user = getReactiveDBUser(currentUser)
                .flatMap(u -> SessionUtil.setUserToSession(webFilterExchange.getExchange(), u));
        Mono<String> requestURL = getRedirectURL(webFilterExchange);

        //Consume
        return user
                .then(requestURL)
                .defaultIfEmpty("/")
                .map(URI::create)
                .map(uri -> {
                    setLocation(uri);
                    return uri;
                })
                .then(
                        super.onAuthenticationSuccess(webFilterExchange, authentication)
                ).then();
    }

}
