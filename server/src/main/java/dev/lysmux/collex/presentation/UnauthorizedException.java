package dev.lysmux.collex.presentation;

import dev.lysmux.collex.domain.AppException;

public class UnauthorizedException extends AppException {
    public UnauthorizedException() {
        super("Login or password is incorrect");
    }
}
