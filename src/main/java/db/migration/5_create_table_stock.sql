create type move as enum ('ENTER', 'EXIT');

create table stock (
    id_stock int primary key,
    move_type move,
    quantity_ingredient_available float,
    unit unit,
    date_of_move TIMESTAMP,
    id_ingredient int,
    constraint fk_ingredient foreign key (id_ingredient) references ingredient (id_ingredient)
);