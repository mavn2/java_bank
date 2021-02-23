package me.max.dao;

import java.sql.Connection;
import java.sql.SQLException;

import me.max.model.User;

public interface UserDAO {
	
	User getUserByUsername(Connection con, String username) throws SQLException;
}
