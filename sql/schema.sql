create table airport
(
    airport_id   varchar(20) not null primary key,
    airport_name varchar(100),
    data         json,
    status       integer,
    version      integer,
    created_at   timestamp,
    created_by   varchar(60),
    updated_at   timestamp,
    updated_by   varchar(60)
);