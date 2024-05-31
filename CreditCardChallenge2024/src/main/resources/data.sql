#POR AHORA NO SE USA. LO TENIA CUANDO ARRANQUé TODO CON DB H2, PERO AHORA LO HAGO DISTINTO POR MYSQL.

INSERT INTO brand_entity (id, name, internacional) VALUES (1, 'Visa', true);
INSERT INTO brand_entity (id, name, internacional) VALUES (2, 'Nara', false);
INSERT INTO brand_entity (id, name, internacional) VALUES (3, 'Amex', true);

INSERT INTO credit_card (id, card_holder, numero, fecha_vencimiento, brand_id) VALUES (1, 'Carlos Gil', '1234567890123456', '2025-05-30', 1);