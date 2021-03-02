package me.max.services;

import java.sql.Connection;
import java.sql.SQLException;

import me.max.dao.UserDAO;
import me.max.dao.UserDAOImpl;
import me.max.exceptions.UserCreationException;
import me.max.exceptions.UserNotFoundException;
import me.max.model.User;
import me.max.util.ConnectionUtil;

public class UserService {
	// U.DAO available when instantiated
	public UserDAO userDAO;

	// Custom constructor
	public UserService() {
		this.userDAO = new UserDAOImpl();
	}

	// Constructor for use in testing, allows mock DAO to be entered as param
	public UserService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	// Searches for a user in db by username, returns User object containing
	// retrieved data or throws exception
	public User getUserByUsername(String username) throws SQLException, UserNotFoundException {
		// Try handles closing connection
		try (Connection con = ConnectionUtil.getConnection()) {
			// Assign result of db query to variable
			User result = userDAO.getUserByUsername(con, username);

			if (result == null) {
				throw new UserNotFoundException("Username " + username + " was not found.");
			}

			return result;
		}
	}

	public User createNewUser(String username, String password, String firstName, String lastName, String phoneNumber)
			throws SQLException, UserCreationException {
		try (Connection con = ConnectionUtil.getConnection()) {

			User result = userDAO.insertNewUser(con, username, password, firstName, lastName, phoneNumber);

			if (result == null) {
				throw new UserCreationException("User could not be created.");
			}
			
			return result;
		}
	}
}
