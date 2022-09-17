-- able_io schema

-- !Ups

drop table if exists able_io.users cascade;

create table able_io.users
(
    id serial8 not null,
    email      varchar(100) not null unique,
    password   varchar(128) not null,
    first_name varchar(64)  not null,
    last_name  varchar(64)  not null,
    constraint users_pk
        primary key (id, email)
);

create unique index users_id_index
    on able_io.users (id);

create unique index users_email_index
    on able_io.users (LOWER((email)));

-- !Downs

DROP TABLE able_io.users