
--esto es solamente para probar sin tener que traer todos los files sql, por el momento no se esta usando
INSERT INTO skill (id, name) VALUES (1, 'Kotlin'), (2, 'Spring Boot');
INSERT INTO country (id, name) VALUES (1, 'Costa Rica');
INSERT INTO area_of_interest (id, name) VALUES (1, 'Desarrollo Backend');
INSERT INTO gender (id, name) VALUES (1, 'Masculino');
INSERT INTO gender (id, name) VALUES (2, 'Femenino');


INSERT INTO users (email, password, create_date, enabled, token_expired)
VALUES ('contact@techsolutions.com', '1234', CURRENT_TIMESTAMP, true, false);

INSERT INTO users ( email, password, create_date, enabled, token_expired)
VALUES ('test@student2.com', '1234', CURRENT_TIMESTAMP, true, false);

INSERT INTO users ( email, password, create_date, enabled, token_expired)
VALUES ('contact@techsolutions.com', '1234', CURRENT_TIMESTAMP, true, false);
--ALTER SEQUENCE hibernate_sequence RESTART WITH 4;

INSERT INTO student (id, username, full_name, university, career, profile_pic_url, country_id, gender_id)
VALUES (1, 'jdoe', 'John Doe', 'University of Nowhere', 'Ingenieria en Sistemas', 'https://example.com/jdoe.png', 1, 1);

INSERT INTO student (id, username, full_name, university, career, profile_pic_url, country_id, gender_id)
VALUES (2, 'asmith', 'Alice Smith', 'Tech State University', 'Ingenieria en Sistemas', 'https://example.com/asmith.png', 1, 2);


-- Company
INSERT INTO company (id, name, website, picture_url)
VALUES (3, 'Tech Solutions', 'https://techsolutions.com', 'https://techsolutions.com/logo.png');


