INSERT INTO country (id, name) VALUES (1, 'Costa Rica');
INSERT INTO country (id, name) VALUES (2, 'Argentina');
INSERT INTO country (id, name) VALUES (3, 'México');
INSERT INTO country (id, name) VALUES (4, 'Colombia');


INSERT INTO gender (id, name) VALUES (1, 'Masculino');
INSERT INTO gender (id, name) VALUES (2, 'Femenino');

INSERT INTO skill (id, name) VALUES (1, 'Kotlin'), (2, 'Spring Boot');


INSERT INTO area_of_interest (id, name) VALUES (1, 'Desarrollo Backend');
INSERT INTO area_of_interest (id, name) VALUES (2, 'Desarrollo Frontend');


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




-- Student
INSERT INTO student (id, username, full_name, university, career, profile_pic_url, country_id, gender_id)
VALUES (1, 'jdoe', 'John Doe', 'University of Nowhere', 'Ingenieria en Sistemas', 'https://example.com/jdoe.png', 1, 1);

INSERT INTO student (id, username, full_name, university, career, profile_pic_url, country_id, gender_id)
VALUES (2, 'asmith', 'Alice Smith', 'Tech State University', 'Ingenieria en Sistemas', 'https://example.com/asmith.png', 1, 2);

--
INSERT INTO student_skill (student_id, skill_id) VALUES (1, 1), (1, 2);
--
INSERT INTO student_areas_of_interest (student_id, area_of_interest_id) VALUES (1, 1);

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

INSERT INTO internship (id, title,short_description, description, company_id, active, category_id)
VALUES (1,'Software Engineering Intern','Gain hands-on experience in software development.',
        'This internship provides an opportunity to work on real-world projects using modern technologies like Kotlin, Android, and REST APIs.', 5, true,1);

INSERT INTO internship (id, title,short_description, description, company_id, active, category_id)
VALUES (2, 'Frontend Developer Intern','Explore data analytics and machine learning.',
        'Work on data-driven projects involving Python, TensorFlow, and SQL to analyze and visualize large datasets.', 6, true,1);

INSERT INTO internship (id, title,short_description, description, company_id, active, category_id)
VALUES (3, 'AI Research Intern', 'Learn about network security and ethical hacking.',
        'Engage in hands-on security assessments, penetration testing, and vulnerability analysis in a corporate environment.', 6, true,1 );




-- ApplicationStatus
INSERT INTO application_status (id, name) VALUES (1, 'Pending'), (2, 'Accepted'), (3, 'Rejected');


INSERT INTO application (id, student_id, internship_id, application_status_id)
VALUES (1, 1, 1, 1); -- Student 1 applies to Internship 1 with status 1

INSERT INTO application (id, student_id, internship_id, application_status_id)
VALUES (2, 2, 1, 2); -- Student 2 applies to Internship 1 with status 2

INSERT INTO application (id, student_id, internship_id, application_status_id)
VALUES (3, 1, 2, 1); -- Student 1 also applies to Internship 2




INSERT INTO role (id, name) VALUES (1, 'Estudiante'), (2, 'Empresa');


INSERT INTO privilege (id, name) VALUES (1, 'administrator');


--sin  @GeneratedValue(strategy = GenerationType.IDENTITY) como que da problemas, no crea la seq
SELECT setval('users_id_seq',(SELECT MAX(id) FROM users));
SELECT setval('application_id_seq',(SELECT MAX(id) FROM application));
SELECT setval('internship_id_seq', (SELECT MAX(id) FROM internship));
SELECT setval('role_id_seq', (SELECT MAX(id) FROM role));
SELECT setval('privilege_id_seq', (SELECT MAX(id) FROM privilege));


/* esto para ver con joel porque son roles y le agregue un rol a user 1
INSERT INTO role (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO role (id, name) VALUES (2, 'ROLE_STAFF');
INSERT INTO role (id, name) VALUES (3, 'ROLE_USER');

INSERT INTO  user_role (user_id, role_id) VALUES (1, 3);
*/
/*
bcript pass 123456
$2a$10$hN2pykneJzAhovs50K4AheNRbLDwq3zEuIfqramxGfyiiYUKjLgne
*/