package me.max.dao;

import java.sql.Connection;
import java.sql.SQLException;

// Validation functions check the database, but
// return a boolean rather than an object,
// so defined separately from related methods
public interface ValidationDAO {
	public boolean validateUserName(Connection con, String name) throws SQLException;

	public boolean validateUserPassword(Connection con, String pass) throws SQLException;
}
