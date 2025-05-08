package dev.lysmux.collex.dto.response;

public enum ResponseStatus {
    SUCCESS,
    INTERNAL_SERVER_ERROR,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    CONFLICT,
    BAD_REQUEST,

    UNKNOWN_COMMAND,
    BAD_COMMAND_ARGS
}
