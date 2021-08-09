package io.vpv.homeuse.service;

import io.netty.handler.logging.LogLevel;
import io.vpv.homeuse.config.HoneyWellConfig;
import io.vpv.homeuse.exceptions.ApplicationException;
import io.vpv.homeuse.model.APIResponseData;
import io.vpv.homeuse.model.User;
import io.vpv.homeuse.model.honeywell.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Service
public class HoneywellThermostatService {

    final
    HoneyWellConfig config;
    final
    HoneywellService honeywellService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HoneywellThermostatService(HoneyWellConfig config, HoneywellService honeywellService) {
        this.config = config;
        this.honeywellService = honeywellService;
    }

    public Mono<APIResponseData> getLocations(final User user) {
        if (null == user) {
            throw new ApplicationException("User cannot be null");
        }

        if (null == user.getHoneyWellLinkToken()) {
            throw new ApplicationException(String.format("%s has not linked the account with Honeywell", user.getUsername()));
        }


        return getLocationsAPI(user);

    }

    private Mono<APIResponseData> getLocationsAPI(final User user) {
        String endpoint = config.getApi().getLocationsEndpoint()
                .concat("?apikey=")
                .concat(config.getCredentials().getClientId());

        return getLocations(endpoint, user)
                .onErrorResume(
                        e -> {

                            if (e instanceof WebClientResponseException.Unauthorized) {
                                return honeywellService.renewToken(user)
                                        .flatMap(newUser -> getLocations(endpoint, newUser));
                            }
                            throw new ApplicationException("Unable to get Location data", e);
                        }
                );

    }

    private Mono<APIResponseData> getLocations(String endpoint, User user) {
        final HttpClient httpClient = HttpClient.create()
                .wiretap(this.getClass().getCanonicalName(), LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
        final ClientHttpConnector conn = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder()
                .baseUrl(endpoint)
                .clientConnector(conn)
                .build()
                .get()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(user
                        .getHoneyWellLinkToken()
                        .getAccessToken())
                ).retrieve()
                .bodyToFlux(Location.class)
                .collectList()
                .map(loc -> APIResponseData.builder()
                        .user(user)
                        .locations(loc).build());
    }


}
