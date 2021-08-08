package io.vpv.homeuse.controller.api;

import io.vpv.homeuse.model.User;
import io.vpv.homeuse.model.honeywell.Location;
import io.vpv.homeuse.service.HoneywellThermostatService;
import io.vpv.homeuse.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;

@RestController
public class HoneywellAPI {
    final
    UserService userService;
    private final HoneywellThermostatService honeywellThermostatService;

    public HoneywellAPI(UserService userService, HoneywellThermostatService honeywellThermostatService) {
        this.userService = userService;
        this.honeywellThermostatService = honeywellThermostatService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/honeywell/locations")
    public ResponseEntity<Mono<List<Location>>> getLocations(ServerWebExchange serverWebExchange) {
        Mono<User> user = serverWebExchange
                .getSession()
                .mapNotNull(
                        session -> session.getAttribute(LOGGED_IN_USER)
                );
        Mono<List<Location>> locations =
                user.flatMap(u -> honeywellThermostatService.getLocations(u)
                        .map(apiResponse -> {
                            serverWebExchange.getAttributes().put(LOGGED_IN_USER, apiResponse.getUser());
                            return apiResponse.getLocations();
                        }));

//        User newUser = userService.findById(user.getId());
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    private void processUser(User user) {
        //We only ned to get from DB and save it in session

//        return Mono.just(locations);
    }
}
