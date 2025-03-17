create table history_price (
    id_ingredient int,
    ingredient_price int,
    date_price Date,
    constraint fk_ingredient foreign key (id_ingredient) references ingredient (id_ingredient)
);