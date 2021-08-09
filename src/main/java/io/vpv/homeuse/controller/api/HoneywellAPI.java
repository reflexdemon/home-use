package io.vpv.homeuse.controller.api;

import io.vpv.homeuse.model.honeywell.Location;
import io.vpv.homeuse.service.HoneywellThermostatService;
import io.vpv.homeuse.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.vpv.homeuse.util.SessionUtil.getUserFromSession;
import static io.vpv.homeuse.util.SessionUtil.setUserToSession;

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
    public Mono<List<Location>> getLocations(ServerWebExchange serverWebExchange) {

        return getUserFromSession(serverWebExchange)
                .flatMap(honeywellThermostatService::getLocations)
                .flatMap(
                        apiResponse -> setUserToSession(serverWebExchange, apiResponse.getUser())
                                .then()
                                .thenReturn(apiResponse.getLocations())
                );
    }
}
