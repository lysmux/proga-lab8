package dev.lysmux.collex.logic.exception;

import dev.lysmux.collex.domain.AppException;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(message);
    }
}
