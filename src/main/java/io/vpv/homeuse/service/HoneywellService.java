package io.vpv.homeuse.service;

import io.vpv.homeuse.config.HoneyWellConfig;
import io.vpv.homeuse.model.HoneyWellLinkToken;
import io.vpv.homeuse.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Service
public class HoneywellService {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    HoneyWellConfig config;

    @Autowired
    UserService userService;

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
        final Map<String, String> params = Map.of(
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

    public Mono<User> getAuthToken(User user, String code, String state, String scope) {
        final String endpoint = config.getOauth().getTokenEndpoint();
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", config.getOauth().getRedirectUrl());

        Consumer<HttpHeaders> headers = (h -> {
            h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            h.setBasicAuth(config.getCredentials().getClientId(), config.getCredentials().getClientSecret());
            h.setAccept(List.of(MediaType.APPLICATION_JSON));
        });


        return postAPI(user, endpoint, map, headers);
    }

    public Mono<User> renewToken(final User user) {
        final String endpoint = config.getOauth().getTokenEndpoint();
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", user.getHoneyWellLinkToken().getRefreshToken());


        Consumer<HttpHeaders> headers = (h -> {
            h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            h.setBasicAuth(config.getCredentials().getClientId(), config.getCredentials().getClientSecret());
//            h.setAccept(List.of(MediaType.APPLICATION_JSON));
        });

        return postAPI(user, endpoint, map, headers);
    }

    private Mono<User> postAPI(User user, String endpoint, MultiValueMap<String, String> map, Consumer<HttpHeaders> headers) {
        return WebClient
                .builder()
                .baseUrl(endpoint)
                .build()
                .post()
                .headers(headers)
                .body(fromFormData(map))
                .retrieve()
                .bodyToMono(HoneyWellLinkToken.class)
                .map(user::withHoneyWellLinkToken)
                .map(userService::save)
                .block();
    }
}
