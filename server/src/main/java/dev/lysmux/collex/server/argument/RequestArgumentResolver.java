package dev.lysmux.collex.server.argument;

import dev.lysmux.collex.dto.request.Request;

import java.lang.reflect.Parameter;
import java.util.Optional;

public class RequestArgumentResolver implements ArgumentResolver<Object> {
    @Override
    public boolean supportsParameter(Parameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(Parameter parameter, Request request) {
        return Optional.ofNullable(request.body().getArgs().get(parameter.getName()))
                .orElseThrow(() -> new ArgumentNotFoundException("No argument found for `%s`".formatted(parameter.getName())));
    }
}
