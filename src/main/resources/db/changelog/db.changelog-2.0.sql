--liquibase formatted sql

--changeset rzakharov:1
INSERT INTO users (login, password)
VALUES ('roza', '$2a$12$R35J.gS0IS7Z.otZXocZoOjsJOZig0QvdUSN.tnPxfs/WtkhJDzQC');