INSERT INTO internship (id, title,short_description, description, company_id)
VALUES (1,'Software Engineering Intern','Gain hands-on experience in software development.',
        'This internship provides an opportunity to work on real-world projects using modern technologies like Kotlin, Android, and REST APIs.', 5);

INSERT INTO internship (id, title,short_description, description, company_id)
VALUES (2, 'Frontend Developer Intern','Explore data analytics and machine learning.',
        'Work on data-driven projects involving Python, TensorFlow, and SQL to analyze and visualize large datasets.', 6);

INSERT INTO internship (id, title,short_description, description, company_id)
VALUES (3, 'AI Research Intern', 'Learn about network security and ethical hacking.',
        'Engage in hands-on security assessments, penetration testing, and vulnerability analysis in a corporate environment.', 6);

SELECT setval('internship_id_seq', (SELECT MAX(id) FROM internship));

