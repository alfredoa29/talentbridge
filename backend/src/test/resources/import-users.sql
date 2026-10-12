INSERT INTO users (id, email, password, create_date, enabled, token_expired)
VALUES (1, 'test@student.com', '1234', CURRENT_TIMESTAMP, true, false);

INSERT INTO users ( id,email, password, create_date, enabled, token_expired)
VALUES (2,'test@student2.com', '1234', CURRENT_TIMESTAMP, true, false);

INSERT INTO users ( id,email, password, create_date, enabled, token_expired)
VALUES (3, 'contact@techsolutions.com', '1234', CURRENT_TIMESTAMP, true, false);

INSERT INTO users ( id,email, password, create_date, enabled, token_expired)
VALUES ( 4,'contact@microsoft.com', 'password123', CURRENT_TIMESTAMP, TRUE, FALSE);

-- Procter & Gamble
INSERT INTO users ( id,email, password, create_date, enabled, token_expired)
VALUES (5, 'internships@pg.com', 'password123', CURRENT_TIMESTAMP, TRUE, FALSE);

-- Google
INSERT INTO users ( id,email, password, create_date, enabled, token_expired)
VALUES ( 6,'careers@google.com', 'password123', CURRENT_TIMESTAMP, TRUE, FALSE);

SELECT setval('users_id_seq', 6, true);
