package me.max.model;

public class Account {
	private final String accountNumber;
	private String accountOwner;
	private String accountType;
	private String accountStatus;
	private double currentBalance;
	private double availableBalance;

	// Custom constructor w/ all fields
	public Account(String accountNumber, String accountOwner, String accountType, String accountStatus,
			double currentBalance, double availableBalance) {
		super();
		this.accountNumber = accountNumber;
		this.accountOwner = accountOwner;
		this.accountType = accountType;
		this.accountStatus = accountStatus;
		this.currentBalance = currentBalance;
		this.availableBalance = availableBalance;
	}

	// Overrriden Methods -toString, getters/setters, hashCode and Equals
	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", accountOwner=" + accountOwner + ", accountType="
				+ accountType + ", accountStatus=" + accountStatus + ", currentBalance=" + currentBalance
				+ ", availableBalance=" + availableBalance + "]";
	}

	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((accountOwner == null) ? 0 : accountOwner.hashCode());
		result = prime * result + ((accountStatus == null) ? 0 : accountStatus.hashCode());
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		long temp;
		temp = Double.doubleToLongBits(availableBalance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(currentBalance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountNumber == null) {
			if (other.accountNumber != null)
				return false;
		} else if (!accountNumber.equals(other.accountNumber))
			return false;
		if (accountOwner == null) {
			if (other.accountOwner != null)
				return false;
		} else if (!accountOwner.equals(other.accountOwner))
			return false;
		if (accountStatus == null) {
			if (other.accountStatus != null)
				return false;
		} else if (!accountStatus.equals(other.accountStatus))
			return false;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (Double.doubleToLongBits(availableBalance) != Double.doubleToLongBits(other.availableBalance))
			return false;
		if (Double.doubleToLongBits(currentBalance) != Double.doubleToLongBits(other.currentBalance))
			return false;
		return true;
	}

}
