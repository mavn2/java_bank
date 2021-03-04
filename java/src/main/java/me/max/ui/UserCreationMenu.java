package me.max.ui;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import me.max.exceptions.UserCreationException;
import me.max.main.Application;
import me.max.model.User;
import me.max.services.UserService;
import me.max.services.ValidationService;

public class UserCreationMenu implements Menu {
	UserService userService;
	ValidationService validationService;

	private static Logger log = Logger.getLogger(UserCreationMenu.class);

	public UserCreationMenu() {
		this.userService = new UserService();
		this.validationService = new ValidationService();
	}

	@Override
	public void display() {
		int choice = 0;

		do {
			System.out.println("===============");
			System.out.println("* CREATE USER *");
			System.out.println("===============");
			System.out.println("1.) Go back");
			System.out.println("2.) Enter your information");

			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
			}

			switch (choice) {
			case 1:
				System.out.println("Shutting down.");
				break;
			case 2:
				User u = makeUser();
				// Automatically log in newly created user
				System.out.println("User creation successful! Logging you in.");
				// Direct them to default user menu
				Application.currentUser = u;
				break;
			default:
				System.out.println("Please enter a valid selection.");
			}
		} while (choice != 1 && Application.currentUser == null);

	}

	private User makeUser() {
		// Vars will store user input, used for DB insertion
		// and creating a new user object
		String username = null;
		String password = null;
		String firstName;
		String lastName;
		String phoneNumber;
		User result = null;

		try {
			do {
				System.out.println("Please enter a username:");
				String potentialName = Menu.sc.next();
				// Check to see if userName is already in database
				try {
					// If input username is unique, save to method var.
					// Otherwise, exception message will display and user prompted again
					username = validationService.checkIfUniqueUsername(potentialName);

				} catch (SQLException | UserCreationException e) {
					System.out.println(e.getMessage());
				}
			} while (username == null);

			// Once username saved, attempt to get a confirmed password
			do {
				System.out.println("Please enter a password:");
				String passA = Menu.sc.next();
				System.out.println("Please enter your password again.");
				String passB = Menu.sc.next();

				try {
					password = validationService.checkIfMatchingPasswords(passA, passB);
				} catch (UserCreationException e) {
					System.out.println(e.getMessage());
				}

			} while (password == null);

			// Once those are verified, take other info
			// These could be checked w/ regex, but currently loose-
			// Less constraints, importance for these values

			System.out.println("Please enter your first name:");
			firstName = Menu.sc.next();

			System.out.println("Please enter your last name:");
			lastName = Menu.sc.next();

			System.out.println("Please enter the digits of your phone number:");
			phoneNumber = Menu.sc.next();

			// Insert user into database, return complete User object
			result = userService.createNewUser(username, password, firstName, lastName, phoneNumber);
		} catch (SQLException | UserCreationException e) {
			System.out.println(e.getMessage());
		}
		log.info("New user " + result + "was created and saved in database.");
		return result;
	}

}
