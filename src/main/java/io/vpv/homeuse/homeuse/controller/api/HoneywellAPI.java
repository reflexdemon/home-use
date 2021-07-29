package io.vpv.homeuse.homeuse.controller.api;

import io.vpv.homeuse.homeuse.config.HoneyWellConfig;
import io.vpv.homeuse.homeuse.service.HoneywellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HoneywellAPI {
    @Autowired
    private HoneywellService honeywellService;

    @RequestMapping(method = RequestMethod.GET, path = "/config/honeywell")
    public ResponseEntity<HoneyWellConfig> getConfig() {
        HoneyWellConfig config = honeywellService.getConfig();
        config.getCredentials().setClientSecret("**********");
        return new ResponseEntity<>(config, HttpStatus.OK);
    }
}
