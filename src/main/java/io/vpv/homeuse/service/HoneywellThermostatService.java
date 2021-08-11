package io.vpv.homeuse.service;

import io.vpv.homeuse.config.HoneyWellConfig;
import io.vpv.homeuse.exceptions.ApplicationException;
import io.vpv.homeuse.model.APIResponseData;
import io.vpv.homeuse.model.User;
import io.vpv.homeuse.model.honeywell.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

import static io.vpv.homeuse.util.HttpUtil.getClientHttpConnector;

@Service
public class HoneywellThermostatService {

    final
    HoneyWellConfig config;
    final
    HoneywellService honeywellService;

    final
    AuditService auditService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HoneywellThermostatService(HoneyWellConfig config, HoneywellService honeywellService, AuditService auditService) {
        this.config = config;
        this.honeywellService = honeywellService;
        this.auditService = auditService;
    }

    public Mono<APIResponseData> getLocations(final User user) {
        if (null == user) {
            throw new ApplicationException("User cannot be null");
        }

        if (null == user.getHoneyWellLinkToken()) {
            throw new ApplicationException(String.format("%s has not linked the account with Honeywell", user.getUsername()));
        }


        return getLocationsAPI(user)
                .map(apiResponse -> {
                    auditService.saveLog(apiResponse.getUser(), Map.of("DATA", "Retrieved Location Data"), "RETRIEVE_LOCATION_DATA");
                    return apiResponse;
                });

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
        return WebClient.builder()
                .baseUrl(endpoint)
                .clientConnector(getClientHttpConnector(this.getClass().getCanonicalName()))
                .build()
                .get()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(user
                        .getHoneyWellLinkToken()
                        .getAccessToken())
                ).retrieve()
                .bodyToFlux(Location.class)
                .cache(Duration.ofHours(2))
                .collectList()
                .map(loc -> APIResponseData.builder()
                        .user(user)
                        .locations(loc).build());
    }


}
