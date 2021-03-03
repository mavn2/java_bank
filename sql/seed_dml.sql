--Seed Data:
--Basic User Classes
INSERT INTO user_types (type_name)
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

--Insert test/default 'Admin' user w
INSERT INTO users 
	(user_name, user_password, first_name, last_name, phone_number, user_type)
VALUES ('Admin', 'password', 'John', 'Doe', '1234567890', 3);

--Test Default Users
INSERT INTO users
	(user_name, user_password, first_name, last_name, phone_number, user_type)
VALUES ('Test1', 'password', 'Jane', 'Doe', '1234567890', 1)

INSERT INTO users
	(user_name, user_password, first_name, last_name, phone_number, user_type)
VALUES ('Test2', 'password', 'Bill', 'Biggs', '1234567890', 1)

--Test user w/ accounts
INSERT INTO users 
	(user_name, user_password, first_name, last_name, phone_number, user_type)
VALUES ('Test3', 'password', 'Cool', 'Customer', '1234567890', 2)

--Test accounts
INSERT INTO bank_app.accounts 
	(account_number, balance, available_balance, account_type, account_owner) 
VALUES ('12345','100','100','Checking','Test3'),
	('12346','1000','1000','Savings','Test3');


INSERT INTO bank_app.account_user (account_number, user_name)
VALUES ('12345','Test3'),
	('12346', 'Test3');

