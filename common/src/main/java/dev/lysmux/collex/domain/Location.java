package dev.lysmux.collex.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Location implements Serializable {
    private Integer x;

    private Float y;

    private String name;
}
