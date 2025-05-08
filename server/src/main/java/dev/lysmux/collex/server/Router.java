package dev.lysmux.collex.server;

import dev.lysmux.collex.dto.request.Request;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.dto.response.ResponseStatus;
import dev.lysmux.collex.server.argument.ArgumentNotFoundException;
import dev.lysmux.collex.server.argument.ArgumentResolver;
import dev.lysmux.collex.server.argument.RequestArgumentResolver;
import dev.lysmux.collex.server.command.registry.CommandNotFoundException;
import dev.lysmux.collex.server.command.registry.CommandRegistry;
import dev.lysmux.collex.server.command.wrapper.BadCommandArgsException;
import dev.lysmux.collex.server.command.wrapper.CommandWrapper;
import dev.lysmux.collex.server.exception.DefaultExceptionHandler;
import dev.lysmux.collex.server.exception.ExceptionHandler;
import dev.lysmux.collex.server.middleware.MiddlewareChain;

import java.lang.reflect.Parameter;
import java.util.Map;

public class Router {
    private final ServerConfig config;
    private final CommandRegistry commandRegistry;

    public Router(CommandRegistry commandRegistry, ServerConfig config) {
        this.commandRegistry = commandRegistry;
        this.config = config;
    }

    public Router(CommandRegistry commandRegistry) {
        this(commandRegistry, ServerConfig.builder().build());
    }

    public Response<?> handleRequest(Request request) {
        CommandWrapper command;
        Response<?> response;
        try {
            command = commandRegistry.getCommand(request.body().getCommand());
        } catch (CommandNotFoundException e) {
            return Response.builder()
                    .status(ResponseStatus.UNKNOWN_COMMAND)
                    .body(new Body<>(e.getMessage()))
                    .build();
        }

        MiddlewareChain chain = new MiddlewareChain(config.getMiddlewares(), request, command);
        if ((response = chain.next()) == null) {
            try {
                response = command.execute(resolveArgs(command, request));
            } catch (BadCommandArgsException | ArgumentNotFoundException e) {
                return Response.builder()
                        .status(ResponseStatus.BAD_COMMAND_ARGS)
                        .body(new Body<>(e.getMessage()))
                        .build();
            } catch (Exception e) {
                return buildErrorResponse(request, e);
            }
        }

        return response;
    }

    private Response<?> buildErrorResponse(Request request, Exception exc) {
        ExceptionHandler exceptionHandler = config.getExceptionHandlers().stream()
                .filter(h -> h.supportsException(exc))
                .findFirst()
                .orElse(new DefaultExceptionHandler());
        return exceptionHandler.handle(request, exc);
    }

    private Object[] resolveArgs(CommandWrapper command, Request request) {
        Parameter[] requestedArgs = command.getArgs();
        Object[] resolvedArgs = new Object[requestedArgs.length];

        for (int i = 0; i < requestedArgs.length; i++) {
            Parameter param = requestedArgs[i];
            ArgumentResolver<?> resolver = config.getArgumentResolvers().stream()
                    .filter(r -> r.supportsParameter(param))
                    .findFirst().orElse(new RequestArgumentResolver());

            resolvedArgs[i] = resolver.resolveArgument(param, request);
        }

        return resolvedArgs;
    }
}
