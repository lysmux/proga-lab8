package dev.lysmux.collex.server.exception;

import dev.lysmux.collex.dto.request.Request;
import dev.lysmux.collex.dto.response.Response;

public interface ExceptionHandler {
    boolean supportsException(Exception exception);

    Response handle(Request request, Exception exception);
}
