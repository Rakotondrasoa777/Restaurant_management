create type unit as enum ('G', 'L', 'U');

create table ingredient (
    id_ingredient int primary key,
    name varchar(200),
    unit_price int,
    unit unit,
    update_datetime timestamp
);