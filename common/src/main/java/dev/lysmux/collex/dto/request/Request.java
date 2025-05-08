package dev.lysmux.collex.dto.request;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record Request(Auth auth, Body body) implements Serializable {}
