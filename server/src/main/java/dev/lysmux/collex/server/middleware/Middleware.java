package dev.lysmux.collex.server.middleware;

import dev.lysmux.collex.dto.request.Request;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.wrapper.CommandWrapper;

public interface Middleware {
    Response handle(Request request, CommandWrapper command, MiddlewareChain chain);
}
