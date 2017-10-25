create table contato (
    id            integer primary key,

    email         varchar(56) not null unique,
    url           varchar(255),
    datetime      varchar(18)
);

create table hibernate_sequences (
    sequence_name          varchar(32) primary key
);