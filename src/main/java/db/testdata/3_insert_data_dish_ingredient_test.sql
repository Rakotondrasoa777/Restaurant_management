INSERT INTO dish_ingredient (id_dish, id_ingredient, required_quantity, unit) VALUES
(1, 1, 100, 'G'),
(1, 2, 0.15, 'L'),
(1, 3, 1, 'U'),
(1, 4, 1, 'U') on conflict do nothing;

INSERT INTO dish_ingredient (id_dish, id_ingredient, required_quantity, unit) VALUES
(2, 3, 1, 'U'),
(2, 2, 0.10, 'L'),
(3, 4, 1, 'U'),
(3, 1, 150, 'G')