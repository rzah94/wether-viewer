--liquibase formatted sql

--changeset rzakharov:1
CREATE TABLE IF NOT EXISTS users
(
    id       serial primary key,
    login    varchar(100) not null unique,
    password varchar(128) not null
);
--rollback DROP TABLE users;

--changeset rzakharov:2
CREATE TABLE IF NOT EXISTS locations
(
    id        serial primary key,
    name      varchar(200) not null unique,
    user_id   int references users (id),
    latitude  decimal      not null,
    longitude decimal      not null,
    unique (latitude, longitude)
);
--rollback DROP TABLE locations

--changeset rzakharov:3
CREATE TABLE IF NOT EXISTS sessions
(
    id         uuid primary key,
    user_id    int references users (id),
    expires_at timestamp not null
);
--rollback DROP TABLE session