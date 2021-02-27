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

SELECT user_id FROM users WHERE user_name = 'Admin' AND user_password = 'password'

INSERT INTO users (user_name, user_password, first_name, last_name, phone_number)
	VALUES ('test123','John','Doe','0123456789')