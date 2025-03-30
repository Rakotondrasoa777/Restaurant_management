INSERT INTO dish (id_dish, name, unit_price) VALUES
    (1, 'Hot Dog', 15000) on conflict do nothing;

INSERT INTO dish (id_dish, name, unit_price) VALUES
    (2, 'Omelette', 8000) on conflict do nothing;

INSERT INTO dish (id_dish, name, unit_price) VALUES
    (3, 'Pain au Saucisse', 10000) on conflict do nothing;
