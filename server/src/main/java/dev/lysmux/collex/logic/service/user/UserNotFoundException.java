package dev.lysmux.collex.logic.service.user;

import dev.lysmux.collex.logic.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String login) {
        super("User not found: %s".formatted(login));
    }

    public UserNotFoundException(int id) {
        super("User not found: ID-%d".formatted(id));
    }
}
