create table dish_order(
    id_dish_order int primary key,
    id_dish int,
    quantity_of_dish int,
    reference_order int,
    constraint fk_dish foreign key (id_dish) references dish (id_dish),
    constraint fk_order foreign key (reference_order) references "order"(reference_order)
);