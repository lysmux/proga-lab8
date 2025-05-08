package dev.lysmux.collex.logic.service.user;

import dev.lysmux.collex.logic.exception.ConflictException;

public class UserExistsException extends ConflictException {
    public UserExistsException(String login) {
        super("User `%s` already exists".formatted(login));
    }
}
