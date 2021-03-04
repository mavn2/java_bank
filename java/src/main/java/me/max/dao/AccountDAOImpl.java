package me.max.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.max.main.Application;
import me.max.model.Account;
import me.max.model.Transfer;

public class AccountDAOImpl implements AccountDAO {

	@Override
	public Account insertAccount(Connection con, String username, double startingBalance, String type)
			throws SQLException {

		Account result;

		// Create new, unique identifying sequence as persistent label for account
		String sql = "INSERT INTO bank_app.accounts (account_number, balance, available_balance, account_type, account_owner) VALUES (?,?,?,?,?);";
		// This wouldn't work in a full scale app, but sufficient for current test
		// purpose
		String accountNumber = UUID.randomUUID().toString().substring(0, 10);

		PreparedStatement ps = con.prepareStatement(sql);

		ps.setString(1, accountNumber);
		ps.setDouble(2, startingBalance);
		ps.setDouble(3, startingBalance);
		ps.setString(4, type);
		ps.setString(5, username);

		int count = ps.executeUpdate();

		if (count == 1) {
			// Currently not implementing multiple users per account,
			// But this keeps that possibility open
			String sqlB = "INSERT INTO INSERT INTO bank_app.account_user (account_number, user_name) VALUES(?,?);";
			PreparedStatement psB = con.prepareStatement(sqlB);
			psB.setString(1, accountNumber);
			psB.setString(2, username);

			// Log successful account creation
			String sqlL = "INSERT INTO bank_app.account_history (account_number, user_name, transaction_des) VALUES (?,?,?);";
			PreparedStatement psL = con.prepareStatement(sqlL);
			psL.setString(1, accountNumber);
			psL.setString(2, username);
			psL.setString(3, "Created");
			psL.executeUpdate();
		}

		result = new Account(accountNumber, username, type, "pending", startingBalance, startingBalance);

		return result;
	}

	// Allows for many to many user/account implementation
	@Override
	public boolean pairAccountAndUser(Connection con, String accountNumber, String username) throws SQLException {
		String sql = "INSERT INTO bank_app.account_user (account_number, user_name) VALUES (?,?);";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, accountNumber);
		ps.setString(2, username);

		int count = ps.executeUpdate();

		// executeUpdate should return 1, the number of rows affected
		if (count != 1) {
			throw new SQLException("Error: could not link account " + accountNumber + " and user " + username);
		}

		return true;
	}

	@Override
	public Account getAccountById(Connection con, String accountNumber) throws SQLException {
		Account result = null;

		String sql = "SELECT * FROM bank_app.accounts WHERE account_number = ?;";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, accountNumber);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			String accountOwner = rs.getString(3);
			String accountType = rs.getString(4);
			String accountStatus = rs.getString(5);
			double balance = rs.getDouble(6);
			double avBalance = rs.getDouble(7);

			result = new Account(accountNumber, accountOwner, accountType, accountStatus, balance, avBalance);
		}

		return result;
	}

	@Override
	public List<Account> getAllAcounts(Connection con) throws SQLException {
		List<Account> result = new ArrayList<>();

		String sql = "SELECT * FROM bank_app.accounts;";

		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		// Iterate over result set, return a list of all Accounts
		while (rs.next()) {
			String accountNumber = rs.getString(2);
			String accountOwner = rs.getString(3);
			String accountType = rs.getString(4);
			String accountStatus = rs.getString(5);
			double balance = rs.getDouble(6);
			double avBalance = rs.getDouble(7);

			Account entry = new Account(accountNumber, accountOwner, accountType, accountStatus, balance, avBalance);
			result.add(entry);
		}

		return result;
	}

	@Override
	public List<Account> getAccountsByStatus(Connection con, String status) throws SQLException {
		List<Account> result = new ArrayList<>();

		String sql = "SELECT * FROM bank_app.accounts WHERE account_status = ?;";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, status);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String accountNumber = rs.getString(2);
			String accountOwner = rs.getString(3);
			String accountClass = rs.getString(4);
			double balance = rs.getDouble(6);
			double avBalance = rs.getDouble(7);

			Account entry = new Account(accountNumber, accountOwner, accountClass, status, balance, avBalance);
			result.add(entry);
		}

		return result;
	}

	@Override
	public List<Account> getAccountsByOwner(Connection con, String userName) throws SQLException {
		List<Account> result = new ArrayList<>();

		String sql = "SELECT * FROM bank_app.accounts WHERE account_owner = ?;";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, userName);
		ResultSet rs = ps.executeQuery();

		// Iterate over result set, return a list of Accounts for user
		while (rs.next()) {
			String accountNumber = rs.getString(2);
			String accountType = rs.getString(4);
			String accountStatus = rs.getString(5);
			double balance = rs.getDouble(6);
			double avBalance = rs.getDouble(7);

			Account entry = new Account(accountNumber, userName, accountType, accountStatus, balance, avBalance);
			result.add(entry);
		}

		return result;
	}

	@Override
	public double getBalanceById(Connection con, String accountNumber) throws SQLException {
		double result;

		String sql = "SELECT balance FROM bank_app.accounts WHERE account_owner = ?";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, accountNumber);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			result = rs.getDouble("balance");
			return result;
		}

		throw new SQLException("Error: could not get balance for account " + accountNumber);
	}

	@Override
	public double getAvailableById(Connection con, String accountNumber) throws SQLException {
		double result;

		String sql = "SELECT available_balance FROM bank_app.accounts WHERE account_owner = ?";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, accountNumber);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			result = rs.getDouble("available_balance");
			return result;
		}

		throw new SQLException("Error: could not get available balance for account " + accountNumber);
	}

	@Override
	public boolean approveAccount(Connection con, String accountNumber) throws SQLException {
		String sql = "UPDATE bank_app.accounts SET account_status = 'Active' WHERE account_number = ?;";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, accountNumber);
		int count = ps.executeUpdate();

		if (count != 1) {
			throw new SQLException("Error: could not activate account " + accountNumber);
		}

		// Log successful activation
		String sqlL = "INSERT INTO bank_app.account_history (account_number, user_name, transaction_des) VALUES (?,?,?);";
		PreparedStatement psL = con.prepareStatement(sqlL);
		psL.setString(1, accountNumber);
		String user = Application.currentUser.getUsername();
		psL.setString(2, user);
		psL.setString(3, "Activated");
		psL.executeUpdate();

		return true;
	}

	@Override
	public boolean accountDeposit(Connection con, String accountNumber, double value) throws SQLException {
		String sql = "UPDATE bank_app.accounts SET balance = balance + (?) WHERE account_number = ?;";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setDouble(1, value);
		ps.setString(2, accountNumber);
		int count = ps.executeUpdate();

		if (count != 1) {
			throw new SQLException("Error: could not deposit " + value + " in account " + accountNumber);
		}

		// Log successful deposit
		String sqlL = "INSERT INTO bank_app.account_history (account_number, user_name, transaction_des) VALUES (?,?,?);";
		PreparedStatement psL = con.prepareStatement(sqlL);
		psL.setString(1, accountNumber);
		String user = Application.currentUser.getUsername();
		psL.setString(2, user);
		psL.setString(3, "Deposit " + value);
		psL.executeUpdate();

		return true;
	}

	@Override
	public boolean accountWithdraw(Connection con, String accountNumber, double value) throws SQLException {
		String sql = "UPDATE bank_app.accounts SET balance = balance - (?) WHERE account_number = ?;";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setDouble(1, value);
		ps.setString(2, accountNumber);

		int count = ps.executeUpdate();

		if (count != 1) {
			throw new SQLException("Error: could not withdraw " + value + " from account " + accountNumber);
		}

		// Log successful withdrawal
		String sqlL = "INSERT INTO bank_app.account_history (account_number, user_name, transaction_des) VALUES (?,?,?);";
		PreparedStatement psL = con.prepareStatement(sqlL);
		psL.setString(1, accountNumber);
		String user = Application.currentUser.getUsername();
		psL.setString(2, user);
		psL.setString(3, "Withdrawal " + value);
		psL.executeUpdate();

		return true;
	}

	@Override
	public boolean aBalDeposit(Connection con, String accountNumber, double value) throws SQLException {
		String sql = "UPDATE bank_app.accounts SET available_balance = available_balance + (?) WHERE account_number = ?;";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setDouble(1, value);
		ps.setString(2, accountNumber);
		int count = ps.executeUpdate();

		if (count != 1) {
			throw new SQLException("Error: could not deposit " + value + " in account " + accountNumber);
		}

		return true;
	}

	@Override
	public boolean aBalWithdraw(Connection con, String accountNumber, double value) throws SQLException {
		String sql = "UPDATE bank_app.accounts SET available_balance = available_balance - (?) WHERE account_number = ?;";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setDouble(1, value);
		ps.setString(2, accountNumber);

		int count = ps.executeUpdate();

		if (count != 1) {
			throw new SQLException("Error: could not withdraw " + value + " from account " + accountNumber);
		}

		return true;
	}

	@Override
	public int startTransfer(Connection con, String userFrom, String accountFrom, String userTo, String accountTo,
			double amount) throws SQLException {
		String sql = "INSERT INTO bank_app.account_transfers (user_from, account_from, user_to, account_to, amount) VALUES (?,?,?,?,?);";
	
		PreparedStatement ps = con.prepareStatement(sql);
		
		ps.setString(1,	userFrom);
		ps.setString(2, accountFrom);
		ps.setString(3, userTo);
		ps.setString(4, accountTo);
		ps.setDouble(5, amount);
		
		int count = ps.executeUpdate();
		
		if(count == 1) {
			// Log transfer start
			String sqlL = "INSERT INTO bank_app.account_history (account_number, user_name, transaction_des) VALUES (?,?,?);";
			PreparedStatement psL = con.prepareStatement(sqlL);
			psL.setString(1, accountFrom);
			psL.setString(2, userFrom);
			psL.setString(3, "Try " + amount + " to " + userTo + ": " + accountTo);
			psL.executeUpdate();
		}
		
		return count;
	}

	@Override
	public int endTransfer(Connection con, int id, String userFrom, String userTo, String accountTo,
			double amount, boolean approval) throws SQLException {
		String sql = "UPDATE bank_app.account_transfers SET pending = FALSE, approved = ? WHERE id = ?;";
		System.out.println(approval);
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setBoolean(1, approval);
		ps.setInt(2, id);
		
		int count = ps.executeUpdate();
		
		if(count == 1) {
			// Log transfer end
			String sqlL = "INSERT INTO bank_app.account_history (account_number, user_name, transaction_des) VALUES (?,?,?);";
			PreparedStatement psL = con.prepareStatement(sqlL);
			psL.setString(1, accountTo);
			psL.setString(2, userTo);
			psL.setString(3, "Settled " + amount + "w/ " + userFrom + ", " + approval);
			psL.executeUpdate();
		}
		
		return count;
	}

	@Override
	public List<Transfer> getPendingTransfersForUser(Connection con, String username) throws SQLException {
		List<Transfer> result = new ArrayList<>();
		
		String sql = "SELECT * FROM bank_app.account_transfers WHERE user_to = ? AND pending = TRUE;";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			int id = rs.getInt(1);
			String userFrom = rs.getString(2);
			String accountFrom = rs.getString(3);
			String userTo = rs.getString(4);
			String accountTo = rs.getString(5);
			double amount = rs.getDouble(6);
			
			Transfer e = new Transfer(id, userFrom, accountFrom, userTo, accountTo, amount, true);
			result.add(e);
		}
		return result;	
	}
}
