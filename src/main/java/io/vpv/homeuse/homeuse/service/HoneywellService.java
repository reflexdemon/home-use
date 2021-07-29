package io.vpv.homeuse.homeuse.service;

import io.vpv.homeuse.homeuse.config.HoneyWellConfig;
import io.vpv.homeuse.homeuse.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class HoneywellService {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    HoneyWellConfig config;

    public HoneyWellConfig getConfig() {
        return config;
    }

    public String getAuthorizeLink(final User user) {
        if (isNull(user)
                || isNull(user.getHoneyWellLinkToken())
                || isNull(user.getHoneyWellLinkToken().getAccessToken())
        ) {
            String redirectEndpoint = buildRedirectEndpoint(user);
            logger.info("The redirect endpoint for {}  is {}", user.getId(), redirectEndpoint);
            return redirectEndpoint;
        }
        return "/?already-linked";
    }

    private String buildRedirectEndpoint(final User user) {
        final String endpoint = config.getOauth().getAuthorizeEndpoint();
        Map<String, String> params = Map.of(
                "response_type", "code",
                "redirect_uri", config.getOauth().getRedirectUrl(),
                "client_id", config.getCredentials().getClientId(),
                "state", user.getId()
        );
        String queryString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + encodeValue(entry.getValue()))
                .collect(Collectors.joining("&"));

        return new StringJoiner("?").add(endpoint)
                .add(queryString)
                .toString();
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            logger.error("Problem encoding {}", value, e);
        }
        return "";
    }

    public User getAuthToken(User user, String code, String state, String scope) {
        //TODO: Need to get the Token based on the code and attach the token to the user
        return user;
    }
}
