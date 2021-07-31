package io.vpv.homeuse.config;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HoneyWellApiConfig {
    String locationsEndpoint;
    String thermostatsEndpoint;
}
