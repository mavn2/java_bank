package me.max.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
			int type = rs.getInt("user_class");

			// Use those to create new User object
			result = new User(userName, firstName, lastName, phoneNumber, type);
		}

		return result;

	}

	@Override
	public User insertNewUser(Connection con, String username, String password, String firstName, String lastName, String phoneNumber)
			throws SQLException {
		@SuppressWarnings("unused") //given an object to reference and returned
		User result = null;
		
		String sql = "INSERT INTO bank_app.users (user_name, user_password, first_name, last_name, phone_number)"
				+ "	VALUES (?,?,?,?,?)";
		
		//Insert param values into prepared string
		PreparedStatement ps = con.prepareStatement(sql);
		
		ps.setString(1, username);
		ps.setString(2, password);
		ps.setString(3, firstName);
		ps.setString(4, lastName);
		ps.setString(5, phoneNumber);
		
		//If succesful, executeUpdate() should return 1
		int count = ps.executeUpdate();
		
		if (count!= 1) {
			throw new SQLException("Account creation failed.");
		}
		
		//Create new user-all info required is already present, since type will be default
		int type = 1;
		return result = new User(username, firstName, lastName, phoneNumber, type);
	}

}
