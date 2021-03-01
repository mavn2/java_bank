--Seed Data:
--Basic User Classes
INSERT INTO user_classes (class_name)
VALUES
('Default'),
('Customer'),
('Employee');

--Basic Account Types
INSERT INTO account_types (type_name)
VALUES
('Checking'),
('Savings'),
('Business');

INSERT INTO account_statuses (status_name)
VALUES
('Pending'),
('Active'),
('Closed');

--Insert test/default 'Admin' user 
INSERT INTO users 
	(user_name, user_password, first_name, last_name, phone_number, user_class)
VALUES ('Admin', 'password', 'John', 'Doe', '1234567890', 3);

INSERT INTO bank_app.accounts 
	(account_number, balance, available_balance, account_type, account_owner) 
VALUES ('testi11','100','100','Checking','Admin');

INSERT INTO bank_app.account_user (account_number, user_name)
VALUES ('testi11','Admin');
