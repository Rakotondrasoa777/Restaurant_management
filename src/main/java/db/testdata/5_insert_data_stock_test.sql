INSERT INTO stock VALUES
(1,'ENTER', 100, 'U', '2025-02-01 08:00:00', 3),
(2, 'ENTER', 50, 'U', '2025-02-01 08:00:00', 4),
(3, 'ENTER', 10000, 'G', '2025-02-01 08:00:00', 1),
(4, 'ENTER', 20, 'L', '2025-02-01 08:00:00', 2) on conflict do nothing;

INSERT INTO stock VALUES
(5, 'EXIT', 10, 'U', '2025-02-02 10:00:00', 3),
(6, 'EXIT', 10, 'U', '2025-02-03 15:00:00', 3),
(7, 'EXIT', 20, 'G', '2025-02-05 16:00:00', 4) on conflict do nothing;