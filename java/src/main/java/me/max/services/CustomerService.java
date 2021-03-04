package me.max.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import me.max.dao.AccountDAO;
import me.max.dao.AccountDAOImpl;
import me.max.dao.CustomerDAO;
import me.max.dao.CustomerDAOImpl;
import me.max.exceptions.AccountCreationException;
import me.max.exceptions.TransactionException;
import me.max.main.Application;
import me.max.model.Account;
import me.max.model.Customer;
import me.max.model.Transfer;
import me.max.model.User;
import me.max.util.ConnectionUtil;

public class CustomerService {
	public AccountDAO accountDAO;
	public CustomerDAO customerDAO;
	private static Logger log = Logger.getLogger(CustomerService.class);

	// Default Constructer, instantiates new DAO object w/ implemented methods
	public CustomerService() {
		this.accountDAO = new AccountDAOImpl();
		this.customerDAO = new CustomerDAOImpl();
	}

	// Constructor for use in testing, allows mock DAO to be entered as param
	public CustomerService(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	public Account requestNewAccount(User user, double startingBalance, String type)
			throws AccountCreationException, SQLException {
		// Ensure starting balance is valid
		if (startingBalance <= 0) {
			throw new AccountCreationException("Error: Account's initial balance should be greater than zero");
		}

		// Try with resources handles connection
		// Attempt to insert account into db

		try (Connection con = ConnectionUtil.getConnection()) {

			Account result = accountDAO.insertAccount(con, user.getUsername(), startingBalance, type);

			if (result == null) {
				log.error("Error: account creation by user " + Application.currentUser + " failed.");
				throw new AccountCreationException("Error: Account creation failed");
			}

			log.info(Application.currentUser + "requested an account.");
			return result;
		}
	}

	public List<Account> getUserAccounts(User user) throws SQLException {
		try (Connection con = ConnectionUtil.getConnection()) {
			List<Account> result = accountDAO.getAccountsByOwner(con, user.getUsername());
			log.info("Retrieved accounts for user " + user.getUsername());
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
			// ensures db operations have succeeded, then commit transaction;
			if (checkA && checkB) {
				con.commit();
			} else {
				con.rollback();
				throw new TransactionException("Error: could not deposit " + amount + " from " + user.getUsername()
						+ " in " + account.getAccountNumber());
			}
		}

		account.setAvailableBalance(account.getAvailableBalance() + amount);
		account.setCurrentBalance(account.getCurrentBalance() + amount);
		log.info(user.getUsername() + " deposited " + amount + " into " + account.getAccountNumber());
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
		log.info(user.getUsername() + " withdrew " + amount + " from " + account.getAccountNumber());
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
				// restore original availableBalance on object if transaction fails/is rolled
				// back
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

	public Customer getCustomerAccount(String username) throws SQLException {
		Customer result;

		try (Connection con = ConnectionUtil.getConnection()) {
			result = customerDAO.getCustomerByUsername(con, username);
			log.info("Created Customer object for " + username);
			return result;
		}
	}

	public void startTransfer(User user, Account account, String userTo, String accountTo, double amount)
			throws SQLException {
		User u = user;
		Account a = account;

		try (Connection con = ConnectionUtil.getConnection()) {
			con.setAutoCommit(false);

			int count = accountDAO.startTransfer(con, u.getUsername(), a.getAccountNumber(), userTo, accountTo, amount);
			if (count == 1) {
				boolean check = accountDAO.aBalWithdraw(con, account.getAccountNumber(), amount);
				if (check == true) {
					double current = a.getCurrentBalance();
					a.setAvailableBalance(current - count);
					con.commit();
					log.info("User " + u.getUsername() + " sent transfer for " + amount + " to " + userTo);
				}
			}
		} catch (SQLException e) {
			log.error(e);
			throw e;
		}
	}

	public void endTransfer(Account account, Transfer transfer, boolean approval) throws SQLException {
		try (Connection con = ConnectionUtil.getConnection()) {
			con.setAutoCommit(false);

			int count = accountDAO.endTransfer(con, transfer.getId(), transfer.getuFrom(), transfer.getuTo(),
					transfer.getaTo(), transfer.getAmount(), approval);
			if (count == 1 && approval == true) {

				boolean check = accountDAO.aBalDeposit(con, transfer.getaTo(), transfer.getAmount());
				boolean checkB = accountDAO.accountDeposit(con, transfer.getaTo(), transfer.getAmount());

				double current = account.getCurrentBalance();

				account.setCurrentBalance(current + transfer.getAmount());
				account.setAvailableBalance(current + transfer.getAmount());

				if (check == true && checkB == true) {
					log.info(transfer.getuTo() + " accepted transfer from " + transfer.getuFrom());
					con.commit();
				}

			} else if (count == 1 && approval == false) {
				boolean checkC = accountDAO.aBalDeposit(con, transfer.getaFrom(), transfer.getAmount());
				if (checkC == true) {
					log.info(transfer.getuTo() + " declined transfer from " + transfer.getuFrom());
					con.commit();
				}

			} else {
				con.rollback();
			}
		} catch (SQLException e) {
			log.error(e);
			throw e;
		}
	}

	public List<Transfer> getPendingTransfers(User u) throws SQLException {
		try (Connection con = ConnectionUtil.getConnection()) {
			List<Transfer> result = new ArrayList<>();

			result = accountDAO.getPendingTransfersForUser(con, u.getUsername());
			log.info(u.getUsername() + " requested their pending transfers");
			return result;
		} catch (SQLException e) {
			log.error(e);
			throw e;
		}
	}
}
