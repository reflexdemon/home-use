package io.vpv.homeuse.homeuse.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationPropertiesScan("io.vpv.homeuse.homeuse.config")
@EnableConfigurationProperties(HoneyWellConfig.class)
@Configuration
public class ConfigProperties {
}
