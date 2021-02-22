package me.max.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Contains methods for validating user credentials @ login
public class ValidationDAOImpl implements ValidationDAO {

	@Override
	public boolean validateUserName(Connection con, String name) throws SQLException {
		// Possible sql errors are handled within the method,
		// lets LoginMenu object continue running/give feedback to user
		// Prepared statement accepts limited values, and returns limited data
		String sql = "SELECT user_id FROM bank_app.users WHERE user_name = ?";
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
	public boolean validateUserPassword(Connection con, String pass) {
		// TODO Auto-generated method stub
		return false;
	}

}
