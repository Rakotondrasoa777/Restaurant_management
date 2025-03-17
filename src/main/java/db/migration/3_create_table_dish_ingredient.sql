create table dish_ingredient (
    id_dish int,
    id_ingredient int,
    required_quantity float,
    unit unit,
    constraint fk_dish foreign key (id_dish) references dish (id_dish),
    constraint fk_ingredient foreign key (id_ingredient) references ingredient (id_ingredient)
);