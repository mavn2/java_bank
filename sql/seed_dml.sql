--Seed Data:
--Basic User Classes
INSERT INTO user_classes (class_name)
VALUES
('Default'),
('Customer'),
('Employee');

--Insert test/default 'Admin' user (no special permissions)
INSERT INTO users (user_name, user_password, first_name, last_name, phone_number)
VALUES ('Admin', 'password', 'John', 'Doe', '1234567890');
