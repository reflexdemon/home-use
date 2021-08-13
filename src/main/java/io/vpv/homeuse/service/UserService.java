package io.vpv.homeuse.service;


/******************************************************************************
 * Copyright 2021 reflexdemon                                                 *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software"), *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS    *
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,*
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL    *
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING    *
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER        *
 * DEALINGS IN THE SOFTWARE.                                                  *
 ******************************************************************************/

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
