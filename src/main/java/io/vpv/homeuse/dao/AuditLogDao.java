package io.vpv.homeuse.dao;

import io.vpv.homeuse.model.OAuth2Log;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

@Repository
public interface AuditLogDao extends ReactiveMongoRepository<OAuth2Log, String> {

    /**
     * Fetch all the actions
     *
     * @param username
     * @return
     */
    Flux<OAuth2Log> findByUsername(String username);

    /**
     * Fetch the Top 30 actions
     *
     * @param username
     * @return
     */
    Flux<OAuth2Log> findTop30ByUsernameOrderByTimestampDesc(String username);

    default Map getHeaders(WebFilterExchange request) {
        return request.getExchange()
                .getRequest()
                .getHeaders()
                .entrySet()
                .stream()
                .map(e -> entry(e.getKey(), String.join(";", e.getValue())))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

    }
}
