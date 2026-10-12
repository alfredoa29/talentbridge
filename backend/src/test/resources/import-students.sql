-- Student
INSERT INTO student (id, username, full_name, university, career, profile_pic_url, country_id, gender_id)
VALUES (1, 'jdoe', 'John Doe', 'University of Nowhere', 'Ingenieria en Sistemas', 'https://example.com/jdoe.png', 1, 1);

INSERT INTO student (id, username, full_name, university, career, profile_pic_url, country_id, gender_id)
VALUES (2, 'asmith', 'Alice Smith', 'Tech State University', 'Ingenieria en Sistemas', 'https://example.com/asmith.png', 1, 2);

--
INSERT INTO student_skill (student_id, skill_id) VALUES (1, 1), (1, 2);
--
INSERT INTO student_areas_of_interest (student_id, area_of_interest_id) VALUES (1, 1);
