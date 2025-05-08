package dev.lysmux.collex.logic.exception;

import dev.lysmux.collex.domain.AppException;

public class ConflictException extends AppException {
    public ConflictException(String message) {
        super(message);
    }
}
