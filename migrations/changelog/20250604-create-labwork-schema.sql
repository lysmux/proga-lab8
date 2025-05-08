CREATE TABLE lab_works
(
    id                   serial PRIMARY KEY,
    owner_id             int          NOT NULL REFERENCES users (id),
    created_at           timestamptz  NOT NULL DEFAULT now(),
    name                 varchar      NOT NULL,
    coordinate_x         int          NOT NULL CHECK ( coordinate_x <= 592 ),
    coordinate_y         float8       NOT NULL CHECK ( coordinate_y <= 892 ),
    minimal_point        bigint       NOT NULL CHECK ( minimal_point > 0 ),
    difficulty           varchar(40)  NOT NULL,
    author_name          varchar      NOT NULL,
    author_birthday      date         NOT NULL,
    author_weight        bigint       NOT NULL CHECK ( author_weight > 0 ),
    author_location_x    int          NOT NULL,
    author_location_y    float8       NOT NULL,
    author_location_name varchar(783) NOT NULL
);
-- rollback DROP TABLE lab_works;

