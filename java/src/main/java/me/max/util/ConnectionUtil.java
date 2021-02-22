package me.max.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.Driver;

public class ConnectionUtil {
	// Creates database connection, for use in other classes
	// Passes up SQLException, allowing class dependent handling
	public static Connection getConnection() throws SQLException {
		// Create a connection reference var
		Connection con = null;

		// Int. driver obj from our dependency, connect w/ JDBC
		Driver postgresDriver = new Driver();
		DriverManager.registerDriver(postgresDriver);

		// Get database location/credentials from environmental variables
		String url = System.getenv("db_url");
		String username = System.getenv("db_username");
		String password = System.getenv("db_password");
		
		// Connect to db and assign to con var.
		con = DriverManager.getConnection(url, username, password);

		// Return con, allowing calling class/method etc to use the connection
		return con;
	}
}
