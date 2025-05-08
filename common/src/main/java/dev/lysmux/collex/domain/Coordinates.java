package dev.lysmux.collex.domain;

import lombok.*;

import java.io.Serializable;


@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Coordinates implements Comparable<Coordinates>, Serializable {
    private Integer x;

    private Double y;

    @Override
    public int compareTo(@NonNull Coordinates o) {
        if (x.compareTo(o.x) != 0) return x.compareTo(o.x);
        return y.compareTo(o.y);
    }
}
