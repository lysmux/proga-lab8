CREATE TABLE IF NOT EXISTS users (
    id serial PRIMARY KEY,
    login varchar(20) UNIQUE NOT NULL ,
    hashed_password varchar(56) NOT NULL,
    registration_time timestamptz NOT NULL DEFAULT now()
);
-- rollback DROP TABLE users;
