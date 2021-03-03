package me.max.dao;

import java.sql.Connection;
import java.sql.SQLException;

import me.max.model.Customer;

public interface CustomerDAO {
	Customer getCustomerByUsername(Connection con, String username) throws SQLException;
}
