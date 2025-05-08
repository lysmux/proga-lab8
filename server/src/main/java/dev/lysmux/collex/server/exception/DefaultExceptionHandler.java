package dev.lysmux.collex.server.exception;

import dev.lysmux.collex.dto.request.Request;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.dto.response.ResponseStatus;

public class DefaultExceptionHandler implements ExceptionHandler {
    @Override
    public boolean supportsException(Exception exception) {
        return true;
    }

    @Override
    public Response<?> handle(Request request, Exception exception) {
        return Response.builder()
                .status(ResponseStatus.INTERNAL_SERVER_ERROR)
                .body(new Body<>(exception.getMessage()))
                .build();
    }
}
