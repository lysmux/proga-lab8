package dev.lysmux.collex.dto.response;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record Response<T>(
        ResponseStatus status,
        Body<T> body
) implements Serializable {
}
