package io.vpv.homeuse.homeuse.dao;


import io.vpv.homeuse.homeuse.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserDao extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String username);
    Flux<User> findByEmail(String email);
}
