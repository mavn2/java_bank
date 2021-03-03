package me.max.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
	// Array List to store account information
	private List<Account> accounts = new ArrayList<Account>();

	// Custom 'setter' method, add single account to Customer object
	// This seems a bit expensive and I may never use it, but it was fun to write
	public void addOnAccount(Account account) {
		ArrayList<Account> currentAccounts = (ArrayList<Account>) this.getAccounts();
		currentAccounts.add(account);
		this.setAccounts(currentAccounts);
	}

	public Customer() {
		super();
	}

	public Customer(ArrayList<Account> accounts) {
		super();
		this.accounts = accounts;
	}

	public Customer(String username, String firstName, String lastName, String phoneNumber, int type) {
		super(username, firstName, lastName, phoneNumber, type);
	}

	public Customer(String username, String firstName, String lastName, String phoneNumber, int type,
			ArrayList<Account> accounts) {
		super(username, firstName, lastName, phoneNumber, type);
		this.accounts = accounts;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

}
