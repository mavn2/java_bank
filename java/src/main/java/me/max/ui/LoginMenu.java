package me.max.ui;

import java.sql.SQLException;

import me.max.exceptions.UserNotFoundException;
import me.max.services.ValidationService;

public class LoginMenu implements Menu {

	public ValidationService validation;

	public LoginMenu() {
		this.validation = new ValidationService();
	}

	@Override
	public void display() {

		String username = null;

		do {
			String inputname;
			System.out.println("================");
			System.out.println("Sign In");
			System.out.println("================");
			System.out.println("Please Enter Your Username");
			// need a back option-q or 1?
			inputname = sc.next();
			try {
				validation.validateUsername(inputname);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} catch (UserNotFoundException e) {
				System.out.println(e.getMessage());
				System.out.println("Please try again.");
			}
		} while (username == null);
	}

}
