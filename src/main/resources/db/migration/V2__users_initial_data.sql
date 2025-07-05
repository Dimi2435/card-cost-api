-- This file initializes the users table with default users
INSERT INTO app_user (username, password, roles) VALUES 
('user', '$2a$10$3U7MQAgatk0ew8PmQ7ibi.27F8o78FFHse3mW0R8YpOC3AS.4As0e', 'ROLE_USER'),
('admin', '$2a$10$g29vVLk3EMdR2ljnWWNAD.r5RDsQh7OkYBe0EiUretoFAqc./X74e', 'ROLE_ADMIN');
