package dev.lysmux.collex.dto.response;

import java.io.Serializable;

public record Body<T>(
        String message,
        T result
) implements Serializable {
    public Body(String message) {
        this(message, null);
    }
}
