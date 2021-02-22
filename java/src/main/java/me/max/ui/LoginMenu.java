package me.max.ui;

import java.sql.SQLException;

import me.max.exceptions.UserNotFoundException;
import me.max.exceptions.UserPasswordException;
import me.max.services.ValidationService;

public class LoginMenu implements Menu {

	public ValidationService validation;

	public LoginMenu() {
		this.validation = new ValidationService();
	}

	@Override
	public void display() {

		String username = null;
		String password = null;

		// First, check username and save to username variable if valid
		do {
			String inputname;

			System.out.println("================");
			System.out.println("Sign In");
			System.out.println("================");
			System.out.println("Please Enter Your Username");

			inputname = sc.next();
			try {
				validation.validateUsername(inputname);
				username = inputname;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} catch (UserNotFoundException e) {
				System.out.println(e.getMessage());
				System.out.println("Please try again.");
			}
		} while (username == null);
		System.out.println(username);
		// Second, check for password matching saved username
		do {
			String inputpass;

			System.out.println("Please enter password for " + username);

			inputpass = sc.next();

			try {
				validation.validatePassword(username, inputpass);
				password = inputpass;
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			} catch (UserPasswordException e) {
				System.out.println(e.getMessage());
				System.out.println("Please try again");
			}

		} while (password == null);

		Menu userMenu = new UserMenu();

		userMenu.display();
	}
}
