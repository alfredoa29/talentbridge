-- Company
INSERT INTO company (id, name, website, picture_url)
VALUES (4, 'Microsoft', 'https://www.microsoft.com', 'https://i.postimg.cc/1XKRnPNV/microsoft.png');

INSERT INTO company (id, name, website, picture_url)
VALUES (5, 'Procter & Gamble', 'https://www.pg.com', 'https://i.postimg.cc/pdXqFBh0/Procter-Gamble-logo-svg.png');

INSERT INTO company (id, name, website, picture_url)
VALUES (6, 'Google', 'https://www.google.com', 'https://i.postimg.cc/7LWdPRs1/6929234-google-logo-icon.png');


insert into  sector (id, name) VALUES (1,'Tecnologia');
INSERT INTO sector (id, name) VALUES (2,'Salud');
INSERT INTO sector (id, name) VALUES (3,'Educación');
INSERT INTO sector (id, name) VALUES (4,'Finanzas');
INSERT INTO sector (id, name) VALUES (5,'Construcción');


INSERT INTO company_sector (company_id, sector_id)
VALUES (4, 1); -- Suponiendo sector_id 1 es "Software"

INSERT INTO company_sector (company_id, sector_id)
VALUES (5, 2); -- Suponiendo sector_id 2 es "Consumo Masivo"


INSERT INTO company_sector (company_id, sector_id)
VALUES (4, 3); -- Suponiendo sector_id 3 es "Tecnología"
