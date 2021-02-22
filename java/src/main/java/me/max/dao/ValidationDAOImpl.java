package me.max.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Contains methods for validating user credentials @ login
public class ValidationDAOImpl implements ValidationDAO {

	@Override
	public boolean validateUserName(Connection con, String name) throws SQLException {
		// Prepared statement protects against SQL injection
		// Validation methods query/return limited, non-identifying data
		String sql = "SELECT user_class FROM bank_app.users WHERE user_name = ?";

		PreparedStatement ps = con.prepareStatement(sql);

		// Set ? in sql string = to username passed in as param
		ps.setString(1, name);

		// Query the database, and store results
		ResultSet rs = ps.executeQuery();

		// If a result has been returned, return true
		if (rs.next()) {
			return true;
		}

		// If above evaluates to false (no results)
		return false;
	}

	@Override
	public boolean validateUserPassword(Connection con, String name, String password) throws SQLException {
		// Functions as above, but accepts and checks both username and password
		// parameters
		String sql = "SELECT user_class FROM bank_app.users WHERE user_name = ? AND user_password = ?";

		PreparedStatement ps = con.prepareStatement(sql);

		ps.setString(1, name);
		ps.setString(2, password);

		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			return true;
		}

		return false;

	}

}
