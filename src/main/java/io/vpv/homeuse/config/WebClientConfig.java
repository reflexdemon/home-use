package io.vpv.homeuse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static io.vpv.homeuse.util.HttpUtil.getClientHttpConnector;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .clientConnector(getClientHttpConnector(this.getClass().getCanonicalName()))
                .build();
    }
}
