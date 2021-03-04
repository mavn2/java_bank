package me.max.ui;

import java.sql.SQLException;

import me.max.exceptions.AccountCreationException;
import me.max.model.User;
import me.max.services.CustomerService;

public class UserMenu implements Menu {

	// Can hold data for user accessing app
	private User u;
	CustomerService customerService;

	// Custom constructor takes a user object as parameter,
	// ensures access to relevant information
	public UserMenu(User user) {
		u = user;
		customerService = new CustomerService();
	}

	@Override
	public void display() {

		int choice = 0;
		
		System.out.println();
		System.out.println("===============");
		System.out.println(" * MAIN MENU *");
		System.out.println("===============");
		System.out.println("Welcome, " + u.getFirstName());
		System.out.println("1.) Log Out and Quit");
		System.out.println("2.) Apply for Account");

		do {
			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
			}

			switch (choice) {
			case 1:
				break;
			case 2:
				requestAccount();
				break;
			default:
				System.out.println("Please enter a valid selection");
			}
		} while (choice != 1);
	}

	private void requestAccount() {
		String accountType = getAccountType();
		Double accountBalance = getStartingBalance();

		int choice = 0;
		boolean selected = false;

		do {
			System.out.println("Are you sure you want to apply for a " + accountType + " account with initial balance "
					+ accountBalance + "?");
			System.out.println("1.) NO, go back");
			System.out.println("2.) YES, apply for account");

			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
			}

			switch (choice) {
			case 1:
				selected = true;
				break;
			case 2:
				selected = true;
				break;

			}
		} while (selected == false);

		if (selected == true && choice == 2) {
			try {
				customerService.requestNewAccount(u, accountBalance, accountType);
				System.out.println(
						"Application accepted! We will contact you by phone once your account has been approved.");
			} catch (SQLException | AccountCreationException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private String getAccountType() {
		System.out.println("Please select an Account type from the list below");

		System.out.println("1.) Checking");
		System.out.println("2.) Savings");
		System.out.println("3.) Business");

		int choice = 0;
		boolean selected = false;
		String result = "";

		do {
			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
			}
			switch (choice) {
			case 1:
				result = "Checking";
				selected = true;
				break;
			case 2:
				result = "Savings";
				selected = true;
				break;
			case 3:
				result = "Business";
				selected = true;
				break;
			default:
				System.out.println("Please enter a valid selection");
			}
		} while (selected == false);

		return result;
	}

	private double getStartingBalance() {
		double result = 0.0;

		System.out.println("Please submit a starting balance for your account");
		result = Double.parseDouble(Menu.sc.nextLine());

		return result;
	}

}
