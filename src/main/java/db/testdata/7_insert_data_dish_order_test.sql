insert into dish_order (id_dish_order, id_dish, quantity_of_dish, reference_order) values
( 1,1, 10, 1),
(1, 2, 3, 1),
( 1, 3, 5, 1) on conflict do nothing;
