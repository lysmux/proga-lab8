package dev.lysmux.collex.infrastructure.repository;

import dev.lysmux.collex.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getById(int id);

    Optional<User> getByLogin(String login);

    int add(User user);

    void update(User user);
}
