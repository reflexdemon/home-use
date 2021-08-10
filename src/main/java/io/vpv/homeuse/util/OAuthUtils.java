package io.vpv.homeuse.util;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

import static io.vpv.homeuse.config.security.Constants.PROVIDER_ID;
import static io.vpv.homeuse.util.HttpUtil.getClientHttpConnector;

@Component
public class OAuthUtils {

    private final ReactiveOAuth2AuthorizedClientService authorizedClientService;

    public OAuthUtils(ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    public Mono<OAuth2AuthorizedClient> getAuthorizedClient(OAuth2AuthenticationToken authentication) {
        return authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(), authentication.getName());
    }

    public ExchangeFilterFunction oauth2Credentials(Mono<OAuth2AuthorizedClient> authorizedClient) {
        return ExchangeFilterFunction.ofRequestProcessor(
                clientRequest -> authorizedClient.map(client -> ClientRequest.from(clientRequest)
                        .headers(header -> header.setBearerAuth(client.getAccessToken().getTokenValue()))
                        .build()));
    }

    public Mono<Map<String, String>> getUserAttributes(OAuth2AuthenticationToken authentication) {
        Mono<OAuth2AuthorizedClient> authorizedClient = getAuthorizedClient(authentication);

        return authorizedClient
                .map(client -> client.getClientRegistration().getProviderDetails().getUserInfoEndpoint())
                .filter(Objects::nonNull)
                .flatMap(url -> WebClient.builder()
                        .filter(oauth2Credentials(authorizedClient))
                        .clientConnector(getClientHttpConnector(this.getClass().getCanonicalName()))
                        .build()
                        .get()
                        .uri(url.getUri())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                        })
                ).map(map -> {
                    map.put(PROVIDER_ID, authentication.getAuthorizedClientRegistrationId());
                    return map;
                });
    }
}
