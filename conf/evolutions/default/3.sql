-- able_io schema

-- !Ups

drop table if exists able_io.devices cascade;

create table able_io.devices
(
    id serial8 not null,
    device_uuid varchar(36) not null unique,
    is_registered boolean not null default false,
    device_name varchar(64) not null,
    user_id bigint not null,
    password varchar(128) not null,
    constraint devices_pk
        primary key (id, device_uuid),
    constraint devices_fk foreign key (user_id)
        references able_io.users (id) match full
        on update cascade on delete cascade
);

create unique index devices_uuid_index
    on able_io.devices (id);

create unique index devices_owner_index
    on able_io.devices (user_id);

-- !Downs

DROP TABLE able_io.devices