package io.vpv.homeuse.controller.page;

import io.vpv.homeuse.service.HoneywellThermostatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static io.vpv.homeuse.config.security.Constants.LOCATIONS;
import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;
import static io.vpv.homeuse.util.SessionUtil.getUserFromSession;
import static io.vpv.homeuse.util.SessionUtil.setUserToSession;

@Controller
@RequestMapping({"/location"})
public class LocationsController {
    final HoneywellThermostatService honeywellThermostatService;

    public LocationsController(HoneywellThermostatService honeywellThermostatService) {
        this.honeywellThermostatService = honeywellThermostatService;
    }


    @GetMapping
    public Mono<String> index(Model model, ServerWebExchange serverWebExchange) {


        return getUserFromSession(serverWebExchange)
                .flatMap(user -> honeywellThermostatService.getLocations(user)
                        .map(loc -> {
                                    model.addAttribute(LOCATIONS, loc.getLocations());
                                    model.addAttribute(LOGGED_IN_USER, loc.getUser());
                                    setUserToSession(serverWebExchange, loc.getUser());
                                    return loc.getUser();
                                }
                        )
                ).thenReturn("locations");
    }
}
