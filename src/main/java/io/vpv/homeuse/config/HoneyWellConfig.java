package io.vpv.homeuse.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "io.vpv.honeywell")
public class HoneyWellConfig {
    HoneyWellOAuthConfig oauth;
    HoneyWellApiConfig api;
    HoneyWellCredentialsConfig credentials;
}
