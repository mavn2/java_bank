package me.max.services;

import java.sql.Connection;
import java.sql.SQLException;

import me.max.dao.ValidationDAO;
import me.max.dao.ValidationDAOImpl;
import me.max.exceptions.UserNotFoundException;
import me.max.util.ConnectionUtil;

public class ValidationService {
	// V.DAO object available when this class is instantiated
	public ValidationDAO validationDAO;
	
	//Custom no args constructor creates an object for above
	public ValidationService(){
		this.validationDAO = new ValidationDAOImpl();
	}
	
	// Checks user input against database and returns result
	public boolean validateUsername(String username) throws SQLException, UserNotFoundException {
		//Try with resources ensures connection is closed
		try(Connection con = ConnectionUtil.getConnection()) {
			String name = username;
			
			//Assign the results of query to a variable
			boolean result = validationDAO.validateUserName(con, name);
			
			if(result = false) {
				throw new UserNotFoundException("Username " + name + "does not Exist!");
			}
			
			return result;
		}
	}
}
