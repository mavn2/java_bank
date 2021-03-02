package me.max.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import me.max.dao.AccountDAO;
import me.max.dao.AccountDAOImpl;
import me.max.exceptions.AccountCreationException;
import me.max.exceptions.TransactionException;
import me.max.model.Account;
import me.max.model.User;
import me.max.util.ConnectionUtil;

public class CustomerService {
	public AccountDAO accountDAO;

	// Default Constructer, instantiates new DAO object w/ implemented methods
	public CustomerService() {
		this.accountDAO = new AccountDAOImpl();
	}

	// Constructor for use in testing, allows mock DAO to be entered as param
	public CustomerService(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	public Account requestNewAccount(User user, double startingBalance, int typeSelection)
			throws AccountCreationException, SQLException {
		// Ensure starting balance is valid
		if (startingBalance <= 0) {
			throw new AccountCreationException("Error: Account's initial balance should be greater than zero");
		}

		// Determine account type based on user input in menu
		// No practical reason not to use another switch statement here
		// But if/else seems to fit this logic better somehow
		String type;
		if (typeSelection == 1) {
			type = "Checking";
		} else if (typeSelection == 2) {
			type = "Savings";
		} else if (typeSelection == 3) {
			type = "Business";
		} else {
			throw new AccountCreationException(
					"Error: Input " + typeSelection + " could not be matched to an account type");
		}

		// Try with resources handles connection
		// Attempt to insert account into db

		try (Connection con = ConnectionUtil.getConnection()) {

			Account result = accountDAO.insertAccount(con, user.getUsername(), startingBalance, type);

			if (result == null) {
				throw new AccountCreationException("Error: Account creation failed");
			}

			return result;
		}
	}
	
	public List<Account> getUserAccounts(User user) throws SQLException{
		try(Connection con = ConnectionUtil.getConnection()){
			List<Account> result = accountDAO.getAccountsByOwner(con, user.getUsername());
			return result;
		}
	}

	// User param not used for basic functionality, but needed for logging
	// and could be used for additional security/verification
	public Account transferIntoAccount(Account account, User user, double amount)
			throws SQLException, TransactionException {
		if (amount < 0) {
			throw new TransactionException("Error: Cannot deposit a negative number");
		}

		try (Connection con = ConnectionUtil.getConnection()) {
			con.setAutoCommit(false);
			// Queries are executed in nested try catch to ensure rollback-
			// Can't use a catch all rollback at end of try w/ resources,
			// connection will already be closed
			boolean checkA;
			try {
				checkA = accountDAO.aBalDeposit(con, account.getAccountNumber(), amount);
			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

			boolean checkB;
			try {
				checkB = accountDAO.accountDeposit(con, account.getAccountNumber(), amount);
			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

			// This check should be redundant, but still:
			// ensures db operations have succeeded, then commits transaction;
			if (checkA && checkB) {
				System.out.println("Checked " + checkA + checkB);
				con.commit();
			} else {
				con.rollback();
				throw new TransactionException("Error: could not deposit " + amount + " from " + user.getUsername()
						+ " in " + account.getAccountNumber());
			}
		}

		account.setAvailableBalance(account.getAvailableBalance() + amount);
		account.setCurrentBalance(account.getCurrentBalance() + amount);
		return account;
	}

	public Account withdrawFromAccount(Account account, User user, double amount)
			throws TransactionException, SQLException {
		if (amount < 0) {
			throw new TransactionException("Error: Cannot withdraw a negative number");
		}
		// Second check ~should~ never be reached if true,
		// would indicate failure somewhere in application
		// Still, given the nature of banking I would rather have redundant checks, as
		// long as
		// they aren't too costly
		if (account.getAvailableBalance() < amount || account.getCurrentBalance() < amount) {
			throw new TransactionException("Error: Withdrawal would overdraw account");
		}

		try (Connection con = ConnectionUtil.getConnection()) {
			con.setAutoCommit(false);

			boolean checkA;
			try {
				checkA = accountDAO.aBalWithdraw(con, account.getAccountNumber(), amount);
			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

			boolean checkB;
			try {
				checkB = accountDAO.accountWithdraw(con, account.getAccountNumber(), amount);
			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

			if (checkA && checkB) {
				con.commit();
			} else {
				con.rollback();
				throw new TransactionException("Error: could not withdraw " + amount + " for " + user.getUsername()
						+ " from " + account.getAccountNumber());
			}
		}

		account.setAvailableBalance(account.getAvailableBalance() - amount);
		account.setCurrentBalance(account.getCurrentBalance() - amount);
		return account;
	}

	public void transferBetweenAccounts(Account from, Account to, User user, double amount)
			throws TransactionException, SQLException {
		if (amount < 0) {
			throw new TransactionException("Error: Cannot transfer a negative amount");
		}
		if (from.getAvailableBalance() < amount || from.getCurrentBalance() < amount) {
			throw new TransactionException("Error: Withdrawal would overdraw account");
		}
		try (Connection con = ConnectionUtil.getConnection()) {
			con.setAutoCommit(false);
			
			boolean checkA;
			try {
				checkA = accountDAO.aBalWithdraw(con, from.getAccountNumber(), amount);
				from.setAvailableBalance(from.getAvailableBalance() - amount);
			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

			boolean checkB;
			try {
				transferIntoAccount(to, user, amount);
				checkB = true;
			} catch (SQLException | TransactionException e) {
				//restore original availableBalance on object if transaction fails/is rolled back
				from.setAvailableBalance(from.getAvailableBalance() + amount);
				con.rollback();
				throw e;
			}

			boolean checkC;
			try {
				checkC = accountDAO.accountWithdraw(con, from.getAccountNumber(), amount);
			} catch (SQLException e) {
				from.setAvailableBalance(from.getAvailableBalance() + amount);
				con.rollback();
				throw e;
			}

			if (checkA && checkB && checkC) {
				con.commit();
			} else {
				con.rollback();
				throw new TransactionException("Error: could not complete transfer from " + from + " to " + to
						+ " by user " + user.getUsername());
			}
		}

		from.setCurrentBalance(from.getCurrentBalance() - amount);
	}
}
