package io.vpv.homeuse.util;

import io.netty.handler.logging.LogLevel;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

public class HttpUtil {
    public static ClientHttpConnector getClientHttpConnector(String loggerName) {
        return new ReactorClientHttpConnector(
                HttpClient
                        .create()
                        .wiretap(
                                loggerName,
                                LogLevel.INFO,
                                AdvancedByteBufFormat.TEXTUAL
                        )
        );
    }
    
}
