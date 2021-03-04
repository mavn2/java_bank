package me.max.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import me.max.dao.AccountDAO;
import me.max.dao.AccountDAOImpl;
import me.max.dao.UserDAO;
import me.max.dao.UserDAOImpl;
import me.max.exceptions.AccountNotFoundException;
import me.max.main.Application;
import me.max.model.Account;
import me.max.model.User;
import me.max.util.ConnectionUtil;

public class EmployeeService {

	private static Logger log = Logger.getLogger(EmployeeService.class);
	public AccountDAO accountDAO;
	public UserDAO userDAO;

	// General use, custom no-args constructor
	public EmployeeService() {
		this.accountDAO = new AccountDAOImpl();
		this.userDAO = new UserDAOImpl();
	}

	// Testing constructor
	public EmployeeService(AccountDAO accountDAO, UserDAO userDAO) {
		this.accountDAO = accountDAO;
		this.userDAO = userDAO;
	}

	public List<Account> getPendingAccounts() throws SQLException, AccountNotFoundException {
		try (Connection con = ConnectionUtil.getConnection()) {
			List<Account> result = new ArrayList<>();
					result = accountDAO.getAccountsByStatus(con, "Pending");
			if (result.isEmpty()) {
				log.warn("User " + Application.currentUser + " tried to retrieve list of pending accounts, found none");
				throw new AccountNotFoundException("No pending accounts found.");
			}

			log.info("User " + Application.currentUser + " retrieved list of pending accounts");
			return result;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		}

	}

	public boolean approvePendingAccount(String accountNumber) throws SQLException {
		try (Connection con = ConnectionUtil.getConnection()) {
			boolean result = accountDAO.approveAccount(con, accountNumber);
			log.info("User " + Application.currentUser + " approved account " + accountNumber);
			return result;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public boolean promoteUser(String username) throws  SQLException {
		try(Connection con = ConnectionUtil.getConnection()){
			boolean result = userDAO.updateUserStatus(con, username, 2);
			log.info("User " + username + " type set to Customer by " + Application.currentUser);
			return result;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public List<User> getListOfCustomers() throws SQLException {
		try (Connection con = ConnectionUtil.getConnection()) {
			List<User> result = userDAO.getAllUsersByType(con, 2);
			log.info("User " + Application.currentUser + " retrieved list of customers");
			// Chose not to convert results before returning-
			// Account information won't be displayed, and
			// an accurate Customer object could contain significantly more data
			// that is not required here
			return result;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public List<Account> getCustomerAccounts(String userName) throws SQLException {
		try (Connection con = ConnectionUtil.getConnection()) {
			List<Account> result = accountDAO.getAccountsByOwner(con, userName);
			log.info("User " + Application.currentUser + " retrieved info on customer's accounts");
			return result;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}
}
