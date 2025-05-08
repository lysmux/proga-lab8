package dev.lysmux.collex.dto;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
public record UserInfo(
        int id,
        String login,
        LocalDateTime registrationTime
) implements Serializable {}
