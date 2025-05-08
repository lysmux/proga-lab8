package dev.lysmux.collex.server.command.wrapper;

public class NotACommandException extends RuntimeException {
    public NotACommandException(String message) {
        super(message);
    }
}
