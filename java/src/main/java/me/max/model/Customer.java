package me.max.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
	// Array List to store account information
	private List<Account> Accounts;

	public Customer() {
		super();
	}

	public Customer(ArrayList<Account> accounts) {
		super();
		this.Accounts = accounts;
	}

	public Customer(String username, String firstName, String lastName, String phoneNumber, int type,
			ArrayList<Account> accounts) {
		super(username, firstName, lastName, phoneNumber, type);
		this.Accounts = accounts;
	}

	public List<Account> getAccounts() {
		return Accounts;
	}

	public void setAccounts(List<Account> accounts) {
		Accounts = accounts;
	}

}
