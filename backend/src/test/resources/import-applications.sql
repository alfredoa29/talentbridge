
-- ApplicationStatus
INSERT INTO application_status (id, name) VALUES (1, 'Pending'), (2, 'Accepted'), (3, 'Rejected');


INSERT INTO application (id, student_id, internship_id, application_status_id)
VALUES (1, 1, 1, 1); -- Student 1 applies to Internship 1 with status 1

INSERT INTO application (id, student_id, internship_id, application_status_id)
VALUES (2, 2, 1, 2); -- Student 2 applies to Internship 1 with status 2

INSERT INTO application (id, student_id, internship_id, application_status_id)
VALUES (3, 1, 2, 1); -- Student 1 also applies to Internship 2
SELECT setval('application_id_seq', 4, true);