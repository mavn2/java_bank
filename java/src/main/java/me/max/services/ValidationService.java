package me.max.services;

import java.sql.Connection;
import java.sql.SQLException;

import me.max.dao.ValidationDAO;
import me.max.dao.ValidationDAOImpl;
import me.max.exceptions.UserNotFoundException;
import me.max.exceptions.UserPasswordException;
import me.max.util.ConnectionUtil;

public class ValidationService {
	// V.DAO object available when this class is instantiated
	public ValidationDAO validationDAO;

	// Custom no args constructor creates an object for above
	public ValidationService() {
		this.validationDAO = new ValidationDAOImpl();
	}

	// Checks for user input in db and returns true/false
	public boolean validateUsername(String username) throws SQLException, UserNotFoundException {
		// Try with resources ensures connection is closed
		try (Connection con = ConnectionUtil.getConnection()) {
			String name = username;

			// Assign the results of query to a variable
			boolean result = validationDAO.validateUserName(con, name);

			if (result == false) {
				throw new UserNotFoundException("Username " + name + " was not found.");
			}

			return result;
		}
	}

	// Checks for matching username/password in db, returns true/false
	public boolean validatePassword(String username, String password) throws SQLException, UserPasswordException {
		// Functions as above, but takes both a username and password as parameters
		try (Connection con = ConnectionUtil.getConnection()) {
			String name = username;
			String pass = password;

			// Assign the results of query to a variable
			boolean result = validationDAO.validateUserPassword(con, name, pass);

			if (result == false) {
				throw new UserPasswordException("Incorrect password for user " + name);
			}

			return result;
		}
	}
}
