package dev.lysmux.collex.presentation;

import dev.lysmux.collex.domain.AppException;
import dev.lysmux.collex.logic.exception.ConflictException;
import dev.lysmux.collex.logic.exception.NotFoundException;
import dev.lysmux.collex.dto.request.Request;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.dto.response.ResponseStatus;
import dev.lysmux.collex.server.exception.ExceptionHandler;

import java.util.Map;

public class GlobalExceptionHandler implements ExceptionHandler {
    private final Map<Class<? extends AppException>, ResponseStatus> statusMap = Map.ofEntries(
            Map.entry(NotFoundException.class, ResponseStatus.NOT_FOUND),
            Map.entry(ConflictException.class, ResponseStatus.CONFLICT),
            Map.entry(UnauthorizedException.class, ResponseStatus.UNAUTHORIZED)
    );

    @Override
    public boolean supportsException(Exception exception) {
        return AppException.class.isAssignableFrom(exception.getClass());
    }

    @Override
    public Response<?> handle(Request request, Exception exc) {
        ResponseStatus status = statusMap.getOrDefault(exc.getClass(), ResponseStatus.INTERNAL_SERVER_ERROR);
        return Response.builder()
                .status(status)
                .body(new Body<>(exc.getMessage()))
                .build();
    }
}
