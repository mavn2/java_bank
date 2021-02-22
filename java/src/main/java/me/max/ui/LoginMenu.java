package me.max.ui;

import java.sql.SQLException;

import me.max.exceptions.UserNotFoundException;
import me.max.services.ValidationService;

public class LoginMenu implements Menu{
	
	public ValidationService validation;
	public LoginMenu () {
		this.validation = new ValidationService();
	}
	
	@Override
	public void display() {
		int step = 0;
		
		String username;

		do {	
		System.out.println("================");
		System.out.println("Sign In");
		System.out.println("================");
		System.out.println("Please Enter Your Username");
		// need a back option-q or 1?
		// will need custom exception or other user feedback/error msg
		username = sc.next();
		System.out.println(username);
		try {
			validation.validateUsername(username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		} while (step == 0);
	}
	
}
