package io.vpv.homeuse.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HoneyWellOAuthConfig {
    String authorizeEndpoint;
    String accessTokenEndpoint;
    String tokenEndpoint;
    String redirectUrl;
}