package dev.lysmux.collex.server;

import dev.lysmux.collex.server.argument.ArgumentResolver;
import dev.lysmux.collex.server.exception.ExceptionHandler;
import dev.lysmux.collex.server.middleware.Middleware;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class ServerConfig {
    int port;

    @Singular List<Middleware> middlewares;

    @Singular List<ArgumentResolver<?>> argumentResolvers;

    @Singular List<ExceptionHandler> exceptionHandlers;
}
