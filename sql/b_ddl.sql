--Store user types (default, customer, employee)
CREATE TABLE user_types (
id SERIAL PRIMARY KEY,
type_name VARCHAR(50) NOT NULL
--In a future, more complex app, permissions would be specified here for say, acount ownership types and employee levels
);

--Store user info
CREATE TABLE users (
--User login/system info
 user_id SERIAL PRIMARY KEY,
 user_name VARCHAR(50) UNIQUE NOT NULL,
 user_password VARCHAR(20) NOT NULL,
 user_type INT NOT NULL DEFAULT 1,
 --User personal info
 first_name VARCHAR(100) NOT NULL,
 last_name VARCHAR(100) NOT NULL,
 phone_number VARCHAR(10) NOT NULL,
 CONSTRAINT fk_type FOREIGN KEY (user_type) REFERENCES user_types(id)
);

--Seems better to manage validation, account balances in java logic
--For ex, not going to specify accounts can't be negative here
--For one thing, seems like if someone did somehow overdraft/perform invalid op, would want data base to reflect that-
--otherwise, just giving user free credit

--Account info
CREATE TABLE account_types (
	id SERIAL PRIMARY KEY,
	type_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE account_statuses (
	id SERIAL PRIMARY KEY,
	status_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE accounts (
	account_id SERIAL PRIMARY KEY,
	account_number VARCHAR(20) UNIQUE NOT NULL,
	account_owner VARCHAR(50) NOT NULL,
	account_type VARCHAR(20) NOT NULL,
	account_status VARCHAR(20) DEFAULT 'Pending',
	balance MONEY NOT NULL,
	available_balance MONEY NOT NULL,
	CONSTRAINT fk_owner FOREIGN KEY (account_owner) REFERENCES  users(user_name),
	CONSTRAINT fk_type FOREIGN KEY (account_type) REFERENCES account_types(type_name),
	CONSTRAINT fk_status FOREIGN KEY (account_status) REFERENCES account_statuses(status_name) 
);

--Store Account/User pairs, allowing many to many relationships
CREATE TABLE account_user (
	id SERIAL PRIMARY KEY,
	account_number VARCHAR(20) NOT NULL,
	user_name VARCHAR(50) NOT NULL,
	CONSTRAINT fk_account FOREIGN KEY (account_number) REFERENCES accounts(account_number),
	CONSTRAINT fk_user FOREIGN KEY (user_name) REFERENCES users(user_name)
);

CREATE TABLE account_history (
	id SERIAL PRIMARY KEY,
	account_number VARCHAR(20) NOT NULL,
	user_name VARCHAR(50) NOT NULL,
	transaction_des VARCHAR(200) NOT NULL,
	transaction_date TIMESTAMP,
	CONSTRAINT fk_account FOREIGN KEY (account_number) REFERENCES accounts(account_number),
	CONSTRAINT fk_user FOREIGN KEY (user_name) REFERENCES users(user_name)
);

