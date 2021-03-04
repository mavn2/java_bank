package me.max.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.max.model.User;

public class UserDAOImpl implements UserDAO {

	@Override
	public User getUserByUsername(Connection con, String name) throws SQLException {

		User result = null;

		String sql = "SELECT * FROM bank_app.users WHERE user_name = ?";

		PreparedStatement ps = con.prepareStatement(sql);

		ps.setString(1, name);

		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			// Retrieve values from returned object
			String userName = rs.getString("user_name");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			String phoneNumber = rs.getString("phone_number");
			int type = rs.getInt("user_type");

			// Use those to create new User object
			result = new User(userName, firstName, lastName, phoneNumber, type);
		}

		return result;

	}

	@Override
	public User insertNewUser(Connection con, String username, String password, String firstName, String lastName,
			String phoneNumber) throws SQLException {
		@SuppressWarnings("unused") // given an object to reference and returned
		User result = null;

		String sql = "INSERT INTO bank_app.users (user_name, user_password, first_name, last_name, phone_number)"
				+ "	VALUES (?,?,?,?,?);";

		// Insert param values into prepared string
		PreparedStatement ps = con.prepareStatement(sql);

		ps.setString(1, username);
		ps.setString(2, password);
		ps.setString(3, firstName);
		ps.setString(4, lastName);
		ps.setString(5, phoneNumber);

		// If succesful, executeUpdate() should return 1
		int count = ps.executeUpdate();

		if (count != 1) {
			throw new SQLException("Account creation failed.");
		}

		// Type will be one by default
		int type = 1;
		return result = new User(username, firstName, lastName, phoneNumber, type);
	}

	@Override
	public int updateUserStatus(Connection con, String username, int status) throws SQLException {
		String sql = "UPDATE bank_app.users SET user_type = ? WHERE user_name = ?;";
		
		PreparedStatement ps = con.prepareStatement(sql);
		
		ps.setInt(1, status);
		ps.setString(2, username);
		
		int count = ps.executeUpdate();
		// Result not checked, in case user is already customer-
		// given where this method is called, a real failure to update here
		// would mean an effectively fatal issue somewhere in db
		
		return count;
	}
	
	@Override
	public List<User> getAllUsers(Connection con) throws SQLException {
		List<User> result = new ArrayList<>(null);
		
		String sql = "SELECT * FROM bank_app.users";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			User user = new User(rs.getString(2), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(4));
			result.add(user);
		}
		
		return result;
	}
	
	@Override
	public List<User> getAllUsersByType(Connection con, int type) throws SQLException {
		List<User> result = new ArrayList<>(null);
		
		String sql = "SELECT * FROM bank_app.users WHERE user_type = ?;";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, type);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			User user = new User(rs.getString(2), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(4));
			result.add(user);
		}
		
		return result;
	}	
}
