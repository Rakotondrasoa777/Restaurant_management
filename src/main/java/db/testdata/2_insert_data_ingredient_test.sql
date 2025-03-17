INSERT INTO ingredient (id_ingredient, name, unit, update_datetime) VALUES
(1, 'Saucisse', 'G', '2025-02-20 10:00:00'),
(2, 'Huile', 'L', '2025-02-20 10:30:00'),
(3, 'Oeuf', 'U', '2025-02-20 11:00:00'),
(4, 'Pain', 'U', '2025-02-20 11:30:00') on conflict do nothing;