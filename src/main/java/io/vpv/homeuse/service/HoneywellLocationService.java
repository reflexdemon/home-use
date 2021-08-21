package io.vpv.homeuse.service;


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

import io.vpv.homeuse.config.HoneyWellConfig;
import io.vpv.homeuse.exceptions.ApplicationException;
import io.vpv.homeuse.model.LocationAPIResponse;
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
public class HoneywellLocationService {

    final
    HoneyWellConfig config;
    final
    HoneywellService honeywellService;

    final
    AuditService auditService;

    final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    WebClient webClient;

    public HoneywellLocationService(HoneyWellConfig config, HoneywellService honeywellService, AuditService auditService, UserService userService) {
        this.config = config;
        this.honeywellService = honeywellService;
        this.auditService = auditService;
        this.userService = userService;

        String endpoint = config.getApi().getLocationsEndpoint()
                .concat("?apikey=")
                .concat(config.getCredentials().getClientId());
        webClient = buildWebClientForEndpoint(endpoint, this.getClass().getCanonicalName());
    }


    public Mono<LocationAPIResponse> getLocations(final User user) {
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

    private Mono<LocationAPIResponse> getLocationsAPI(final User user) {

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

    private Mono<LocationAPIResponse> getLocationsInternal(User user) {
        return webClient.get()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(user
                        .getHoneyWellLinkToken()
                        .getAccessToken())
                ).retrieve()
                .bodyToFlux(Location.class)
                .cache(Duration.ofHours(2))
                .collectList()
                .map(loc -> LocationAPIResponse.builder()
                        .user(user)
                        .locations(loc).build());
    }


}
