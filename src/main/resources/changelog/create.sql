create table serverconfiguration (
    id                  integer primary key,

    httphostname        varchar(255),
    httpport            integer,
    serverdescription   varchar(255)
);

create table contact (
    id            integer primary key,

    email         varchar(56) not null unique,
    url           varchar(255),
    datetime      varchar(18)
);

create table hibernate_sequences (
    sequence_name          varchar(32) primary key
);

-- Insira as configurações iniciais do servidor ($HOSTNAME, $PORT, $DESCRIPTION)
insert into serverconfiguration values (1, '127.0.0.1', 8080, 'USERTRACKING SERVER');