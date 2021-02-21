--Store user types (default, customer, employee)
CREATE TABLE user_classes (
id SERIAL PRIMARY KEY,
class_name VARCHAR(50) NOT NULL
--In a future, more complex app, permissions would be specified here for say, acount ownership types and employee levels
);

--Store user info
CREATE TABLE users (
--User login/system info
 user_id SERIAL PRIMARY KEY,
 user_name VARCHAR(50) UNIQUE NOT NULL,
 user_password VARCHAR(20) NOT NULL,
 user_class INT NOT NULL DEFAULT 0,
 --User personal info
 first_name VARCHAR(100) NOT NULL,
 last_name VARCHAR(100) NOT NULL,
 phone_number VARCHAR(10) NOT NULL,
 CONSTRAINT fk_class FOREIGN KEY (user_class) REFERENCES user_classes(id)
);

--Seems better to manage validation, account balances in java logic
--For ex, not going to specify accounts can't be negative here
--For one thing, seems like if someone did overdraft/perform invalid op, would want data base to reflect that-
--otherwise, just giving user free credit

--Account info
CREATE TABLE accounts (
	account_id SERIAL PRIMARY KEY,
	balance INT NOT NULL,
	a_balance INT NOT NULL --Maybe this is going to far for now, but I feel like I can work into first build
	--Consider types-saving, checking, etc-would be crucial for real app but also outside any user stories given
);

--Implenting many to many is past mvp I think, but do want capability-
--So here's a table to handle account-owner relationships
CREATE TABLE account_user (
	id SERIAL PRIMARY KEY,
	account_id INT NOT NULL,
	user_id INT NOT NULL,
	CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(account_id),
	CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

