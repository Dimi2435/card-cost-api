-- This file initializes the users table with an admin user
INSERT INTO app_user (username, password, roles) VALUES 
('admin', '{hashed_password}', 'ADMIN');
