package dev.lysmux.collex.client.exception;

import dev.lysmux.collex.dto.response.ResponseStatus;

public class ServerException extends RuntimeException {
    public ServerException(ResponseStatus status, String message) {
        super(message);
    }
}
