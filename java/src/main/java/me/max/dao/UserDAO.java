package me.max.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import me.max.model.User;

public interface UserDAO {

	User getUserByUsername(Connection con, String username) throws SQLException;
	User insertNewUser(Connection con, String username, String password, String firstName, String lastName, String phoneNumber) throws SQLException;
	boolean updateUserStatus(Connection con, String username, int status) throws SQLException;
 	List<User> getAllUsers(Connection con) throws SQLException;
	List<User> getAllUsersByType(Connection con, int type) throws SQLException;	
}
