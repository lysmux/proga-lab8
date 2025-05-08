package dev.lysmux.collex.domain;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;


@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LabWork implements Comparable<LabWork>, Serializable {
    @Builder.Default
    private Integer id = -1;

    @Builder.Default
    private Integer ownerId = -1;

    private String name;

    private long minimalPoint;

    private Difficulty difficulty;

    @Builder.Default
    private LocalDate creationDate = LocalDate.now();

    private Coordinates coordinates;

    private Person author;

    @Override
    public int compareTo(@NonNull LabWork o) {
        return coordinates.compareTo(o.coordinates);
    }
}