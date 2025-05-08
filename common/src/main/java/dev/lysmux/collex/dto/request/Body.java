package dev.lysmux.collex.dto.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public record Body(
        String command,
        Map<String, Object> args
) implements Serializable {
    public Body(String command) {
        this(command, new HashMap<>());
    }
}
