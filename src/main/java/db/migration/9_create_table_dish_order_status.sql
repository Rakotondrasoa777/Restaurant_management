create table dish_order_status (
    status status,
    date_dish_order_status timestamp,
    id_dish_order int,
    constraint fk_dish_order foreign key (id_dish_order) references dish_order (id_dish_order)
);



