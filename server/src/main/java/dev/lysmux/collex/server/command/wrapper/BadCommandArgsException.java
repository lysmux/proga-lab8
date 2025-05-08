package dev.lysmux.collex.server.command.wrapper;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BadCommandArgsException extends RuntimeException {
    private final Class<?>[] requiredArgs;
    private final Class<?>[] providedArgs;

    public BadCommandArgsException(Class<?>[] requiredArgs, Class<?>[] providedArgs) {
        super();

        this.requiredArgs = requiredArgs;
        this.providedArgs = providedArgs;
    }

    @Override
    public String getMessage() {
        String providedArgsString = Arrays.stream(providedArgs)
                .map(Class::getName)
                .collect(Collectors.joining(", "));
        String requiredArgsString = Arrays.stream(requiredArgs)
                .map(Class::getName)
                .collect(Collectors.joining(", "));

        return "Bad command arguments provided. Requires: %s. Provided: %s".formatted(requiredArgsString, providedArgsString);
    }
}
