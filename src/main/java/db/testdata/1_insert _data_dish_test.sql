INSERT INTO dish (id_dish, name, unit_price) VALUES
    (1, 'Hot Dog', 15000) on conflict do nothing;