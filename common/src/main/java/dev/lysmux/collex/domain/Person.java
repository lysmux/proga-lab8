package dev.lysmux.collex.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Person implements Serializable {
    private String name;

    private LocalDate birthday;

    private Long weight;

    private Location location;
}
