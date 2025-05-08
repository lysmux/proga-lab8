package dev.lysmux.collex.infrastructure.repository;

import dev.lysmux.collex.domain.User;

public interface UserRepository {
    User getById(int id);

    User getByLogin(String login);

    int add(User user);

    void update(User user);
}
