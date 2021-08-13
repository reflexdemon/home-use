package io.vpv.homeuse.util;


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
