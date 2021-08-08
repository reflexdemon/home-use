package io.vpv.homeuse.service;

import io.vpv.homeuse.dao.UserDao;
import io.vpv.homeuse.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    final
    UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Mono<User> findById(String id) {
        return userDao.findById(id);
    }

    public Flux<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public Mono<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public Mono<User> save(User user) {
        return userDao.save(user);
    }

    public Mono<Void> deleteById(String id) {
        return userDao.deleteById(id);
    }

    public Mono<Void> deleteByEmail(String email) {
        Flux<User> users = findByEmail(email);
        Optional<Boolean> hasUser = users.any(Objects::nonNull).blockOptional();
        if (hasUser.orElse(false)) {
            return deleteById(users.blockFirst().getId());
        }
        return Mono.empty();
    }
}
