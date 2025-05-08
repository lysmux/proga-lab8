package dev.lysmux.collex.server.middleware;

import dev.lysmux.collex.dto.request.Request;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.wrapper.CommandWrapper;

import java.util.Collection;
import java.util.Iterator;

public class MiddlewareChain {
    private final Iterator<Middleware> middlewares;
    private final Request request;
    private final CommandWrapper command;

    public MiddlewareChain(Collection<Middleware> middlewares, Request request, CommandWrapper command) {
        this.middlewares = middlewares.iterator();
        this.request = request;
        this.command = command;
    }

    public Response next() {
        if (middlewares.hasNext()) {
            return middlewares.next().handle(request, command, this);
        }

        return null;
    }
}
