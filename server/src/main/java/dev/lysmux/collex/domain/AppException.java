package dev.lysmux.collex.domain;

public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }

    public AppException() {}
}
