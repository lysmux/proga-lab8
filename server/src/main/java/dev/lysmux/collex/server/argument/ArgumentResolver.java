package dev.lysmux.collex.server.argument;

import dev.lysmux.collex.dto.request.Request;

import java.lang.reflect.Parameter;

public interface ArgumentResolver<T> {
    boolean supportsParameter(Parameter parameter);
    T resolveArgument(Parameter parameter, Request request);
}
