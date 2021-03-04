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

	@Override
	public String toString() {
		return "Customer [accounts=" + accounts + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((accounts == null) ? 0 : accounts.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (accounts == null) {
			if (other.accounts != null)
				return false;
		} else if (!accounts.equals(other.accounts))
			return false;
		return true;
	}
	
	

}
