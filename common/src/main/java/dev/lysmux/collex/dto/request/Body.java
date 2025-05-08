package dev.lysmux.collex.dto.request;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Builder
@Value
public class Body implements Serializable {
    String command;

    @Singular Map<String, Object> args;
}
