package io.vpv.homeuse.homeuse.service;

import io.vpv.homeuse.homeuse.config.HoneyWellConfig;
import io.vpv.homeuse.homeuse.exceptions.ApplicationException;
import io.vpv.homeuse.homeuse.model.HoneyWellLinkToken;
import io.vpv.homeuse.homeuse.model.User;
import io.vpv.homeuse.homeuse.model.honeywell.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.nimbusds.oauth2.sdk.token.AccessTokenType.BEARER;

@Service
public class HoneywellThermostatService {

    @Autowired
    HoneyWellConfig config;

    @Autowired
    HoneywellService honeywellService;

    @Autowired
    @Qualifier("honeywellRestTemplate")
    RestTemplate restTemplate;


    public List<Location> getLocations(final User user) {
        if (null == user) {
            throw new ApplicationException("User cannot be null");
        }

        if (null == user.getHoneyWellLinkToken()) {
            throw new ApplicationException(String.format("%s has not linked the account with Honeywell", user.getUsername()));
        }

        HoneyWellLinkToken linkToken = user.getHoneyWellLinkToken();

        List<Location> locations = null;
        try {
            locations = getLocations(linkToken);

        } catch (Exception e) {
            User newUser = honeywellService.renewToken(user).block();
            locations = getLocations(newUser);
        }

        return locations;

    }

    private List<Location> getLocations(HoneyWellLinkToken linkToken) {
        String endpoint = config.getApi().getLocationsEndpoint()
                .concat("?apikey=")
                .concat(config.getCredentials().getClientId());
        HttpHeaders headers = new HttpHeaders();
        if (BEARER.toString().equals(linkToken.getTokenType())) {
            headers.setBearerAuth(linkToken.getAccessToken());
        }
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Location>> responseLocations =
                restTemplate.exchange(endpoint, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                });
        List<Location> locations = responseLocations.getBody();
        return locations;
    }
}
