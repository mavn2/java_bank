package me.max.services;

import java.sql.Connection;
import java.sql.SQLException;

import me.max.dao.UserDAO;
import me.max.dao.UserDAOImpl;
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
}
