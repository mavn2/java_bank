package me.max.dao;

import java.sql.Connection;
import java.sql.SQLException;

import me.max.model.User;

public interface UserDAO {

	User getUserByUsername(Connection con, String username) throws SQLException;

	User insertNewUser(Connection con, String username, String password, String firstName, String lastName, String phoneNumber) throws SQLException;
}
