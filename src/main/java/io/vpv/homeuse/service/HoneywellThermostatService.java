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

import static io.vpv.homeuse.util.HttpUtil.buildWebClientForEndpoint;

@Service
public class HoneywellThermostatService {

    final
    HoneyWellConfig config;
    final
    HoneywellService honeywellService;

    final
    AuditService auditService;

    final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    WebClient webClient;

    public HoneywellThermostatService(HoneyWellConfig config, HoneywellService honeywellService, AuditService auditService, UserService userService) {
        this.config = config;
        this.honeywellService = honeywellService;
        this.auditService = auditService;
        this.userService = userService;

        String endpoint = config.getApi().getLocationsEndpoint()
                .concat("?apikey=")
                .concat(config.getCredentials().getClientId());
        webClient = buildWebClientForEndpoint(endpoint, this.getClass().getCanonicalName());
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

        return userService.findById(user.getId())
                .flatMap(dbUser -> getLocationsInternal(dbUser)
                        .onErrorResume(
                                e -> {

                                    if (e instanceof WebClientResponseException.Unauthorized) {
                                        return honeywellService.renewToken(dbUser)
                                                .flatMap(this::getLocationsInternal);
                                    }
                                    throw new ApplicationException("Unable to get Location data", e);
                                }
                        ))
                ;

    }

    private Mono<APIResponseData> getLocationsInternal(User user) {
        return webClient.get()
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
