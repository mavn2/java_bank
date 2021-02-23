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
		
		if(rs.next()) {
			String userName = rs.getString("user_name");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			String phoneNumber = rs.getString("phone_number");
			
			result = new User(userName, firstName, lastName, phoneNumber);
			}
		
		return result;

	}

}
