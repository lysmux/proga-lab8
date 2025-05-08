package dev.lysmux.collex.dto.response;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Builder
@Value
public class Response<T> implements Serializable {
    @Builder.Default
    ResponseStatus status = ResponseStatus.SUCCESS;

    Body<T> body;
}
