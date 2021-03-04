package me.max.ui;

import java.sql.SQLException;
import java.util.List;

import me.max.model.Account;
import me.max.model.User;
import me.max.model.Customer;
import me.max.services.CustomerService;

public class CustomerMenu implements Menu {
	private User u;
	List<Account> accounts;

	CustomerService customerService = new CustomerService();

	public void display() {
		try {
			// Get Customer object for user
			u = customerService.getCustomerAccount(u.getUsername());
			// Can downcast u, use Customer methods for easy access to accounts
			accounts = ((Customer) u).getAccounts();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("===============");
		System.out.println(" * MAIN MENU * ");
		System.out.println("===============");

		System.out.println("Welcome, " + u.getFirstName());
		System.out.println();
		System.out.println("1.) Log Out and Quit");
		System.out.println("2.) View my Accounts");
		System.out.println("3.) Withdraw from Account");
		System.out.println("4.) Deposit to Account");
		System.out.println("5.) Post transfer to Account");
	}

	// Custom no args constructor
	public CustomerMenu(User user) {
		u = user;
		this.customerService = new CustomerService();
	}
}
