package me.max.ui;

import java.sql.SQLException;

import me.max.exceptions.UserNotFoundException;
import me.max.exceptions.UserPasswordException;
import me.max.main.Application;
import me.max.model.User;
import me.max.services.UserService;
import me.max.services.ValidationService;

import org.apache.log4j.Logger;

public class LoginMenu implements Menu {

	public ValidationService validation;
	public UserService userService;
	private String username;
	
	private static Logger log = Logger.getLogger(LoginMenu.class);
	
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
			System.out.println(" * SIGN-IN * ");
			System.out.println("================");
			System.out.println("1.) Back");
			System.out.println("2.) Sign in");

			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
				// No real need to do anything here,
				// default case gives feedback to user
			}

			switch (choice) {
			case 1:
				break;
			case 2:
				getUsername();
				if(username == null) {
					choice = 0;
					break;
				}
				getPassword(username);
				//If user has logged in, set choice to 1 and end execution-exit this menu
				if(Application.currentUser != null) {
					choice = 1;
					break;
				}
				//Otherwise, reset choice to prevent users being stuck at password entry
				choice = 0;
				break;
			default: 
				System.out.println("Please enter an option number.");
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
			username = null;
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
			Application.currentUser= user;
			log.info("User " + username + " logged in");
		} catch (UserPasswordException | SQLException | UserNotFoundException e) {
			username = null;
			System.out.println(e.getMessage());
			log.warn("Login attempt for user " + username + " failed", e);
		}
	}
}
