package io.vpv.homeuse.service;

import io.vpv.homeuse.config.HoneyWellConfig;
import io.vpv.homeuse.exceptions.ApplicationException;
import io.vpv.homeuse.model.User;
import io.vpv.homeuse.model.honeywell.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class HoneywellThermostatService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    HoneyWellConfig config;
    @Autowired
    HoneywellService honeywellService;

    public List<Location> getLocations(final User user) {
        if (null == user) {
            throw new ApplicationException("User cannot be null");
        }

        if (null == user.getHoneyWellLinkToken()) {
            throw new ApplicationException(String.format("%s has not linked the account with Honeywell", user.getUsername()));
        }


        List<Location> locations = getLocationsAPI(user).block();

        return locations;

    }

    private Mono<List<Location>> getLocationsAPI(final User user) {
        String endpoint = config.getApi().getLocationsEndpoint()
                .concat("?apikey=")
                .concat(config.getCredentials().getClientId());

        return WebClient.builder().baseUrl(endpoint)
                .build()
                .get()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(user.getHoneyWellLinkToken().getAccessToken()))
                .retrieve()
                .bodyToFlux(Location.class)
                .collectList()
                .doOnError(err -> {
                    WebClient.builder().baseUrl(endpoint)
                            .build()
                            .get()
                            .headers(httpHeaders -> httpHeaders.setBearerAuth(honeywellService.renewToken(user).block().getHoneyWellLinkToken().getAccessToken()))
                            .retrieve().bodyToFlux(Location.class);

                });

    }


}
