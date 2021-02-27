package me.max.ui;

import java.sql.SQLException;

import me.max.exceptions.UserNotFoundException;
import me.max.exceptions.UserPasswordException;
import me.max.model.User;
import me.max.services.UserService;
import me.max.services.ValidationService;

public class LoginMenu implements Menu {

	public ValidationService validation;
	public UserService userService;
	private String username;
	
	public LoginMenu() {
		this.validation = new ValidationService();
		this.userService = new UserService();
	}

	@Override
	public void display() {

		username = null;

		int choice = 0;

		do {
			System.out.println("================");
			System.out.println("SIGN-IN MENU");
			System.out.println("================");
			System.out.println("1.) Back");
			System.out.println("2.) Sign in");

			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
			}

			switch (choice) {
			case 1:
				break;
			case 2:
				getUsername();
				getPassword(username);
				break;
			default: 
				System.out.println();
			}

		} while (choice != 1);
	}

	private void getUsername() {
		String inputname;

		System.out.println("Please Enter Your Username");

		inputname = sc.next();
		try {
			validation.validateUsername(inputname);
			this.username = inputname;
		} catch (SQLException | UserNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	private void getPassword(String username) {
		String inputpass;

		System.out.println("Please enter password for " + username);

		inputpass = sc.next();

		try {
			validation.validatePassword(username, inputpass);

			User user = userService.getUserByUsername(username);
			Menu userMenu = new UserMenu(user);

			userMenu.display();
		} catch (UserPasswordException | SQLException | UserNotFoundException e) {
			System.out.println(e.getMessage());

		}
	}
}
