-- able_io schema

-- !Ups

drop table if exists able_io.sessions cascade;

create table able_io.sessions
(
    id serial8 not null,
    device_id bigint not null,
    ip_addr inet not null,
    session_value varchar(64) not null,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone,
    constraint sessions_pk primary key (id),
    constraint sessions_fk foreign key (device_id)
        references able_io.devices (id) match full
        on update cascade on delete cascade
);

create unique index session_id_index
    on able_io.sessions (id);

create unique index session_value_index
    on able_io.sessions (session_value);

-- !Downs

DROP TABLE able_io.sessions