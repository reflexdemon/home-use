package io.vpv.homeuse.controller.api;

import io.vpv.homeuse.model.User;
import io.vpv.homeuse.model.honeywell.Location;
import io.vpv.homeuse.service.HoneywellThermostatService;
import io.vpv.homeuse.service.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;

@RestController
public class HoneywellAPI {
    @Autowired
    UserSession session;
    @Autowired
    private HoneywellThermostatService honeywellThermostatService;

    @RequestMapping(method = RequestMethod.GET, path = "/honeywell/locations")
    public ResponseEntity<List<Location>> getLocations() {
        User user = session.getValueFromSession(LOGGED_IN_USER, User.class);
        List<Location> locations = honeywellThermostatService.getLocations(user);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
}
