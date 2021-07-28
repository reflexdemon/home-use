package io.vpv.homeuse.homeuse.service;

import io.vpv.homeuse.homeuse.config.HoneyWellConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HoneywellService {

    @Autowired
    HoneyWellConfig config;

    public HoneyWellConfig getConfig() {
        return config;
    }
}
