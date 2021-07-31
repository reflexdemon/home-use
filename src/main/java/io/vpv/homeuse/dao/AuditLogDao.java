package io.vpv.homeuse.dao;

import io.vpv.homeuse.model.OAuth2Log;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

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
}
