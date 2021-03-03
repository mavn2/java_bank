package me.max.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.max.model.Account;

public class AccountDAOImpl implements AccountDAO {

	@Override
	public Account insertAccount(Connection con, String username, double startingBalance, String type)
			throws SQLException {

		Account result = null;

		// Create new, unique identifying sequence as persistent label for account
		String sql = "INSERT INTO bank_app.accounts (account_number, balance, available_balance, account_type, account_owner) VALUES (?,?,?,?,?);";
		String accountNumber = UUID.randomUUID().toString();

		PreparedStatement ps = con.prepareStatement(sql);

		ps.setString(1, accountNumber);
		ps.setDouble(2, startingBalance);
		ps.setDouble(3, startingBalance);
		ps.setString(4, type);
		ps.setString(5, username);

		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			String status = rs.getString("account_status");
			result = new Account(accountNumber, username, type, status, startingBalance, startingBalance);
		}

		return result;
	}

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
		List<Account> result = new ArrayList<>(null);
	
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
	public List<Account> getAccountsByType(Connection con, String type) throws SQLException {
		List<Account> result = new ArrayList<>(null);
		
		String sql = "SELECT * FROM bank_app.accounts WHERE account_type = ?;";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, type);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String accountNumber = rs.getString(2);
			String accountOwner = rs.getString(3);
			String accountStatus = rs.getString(5);
			double balance = rs.getDouble(6);
			double avBalance = rs.getDouble(7);

			Account entry = new Account(accountNumber, accountOwner, type, accountStatus, balance, avBalance);
			result.add(entry);
		}

		return result;
	}

	@Override
	public List<Account> getAccountsByOwner(Connection con, String userName) throws SQLException {
		List<Account> result = new ArrayList<>(null);

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

		return true;
	}

	@Override
	public boolean accountDeposit(Connection con, String accountNumber, double value) throws SQLException {
		String sql = "UPDATE bank_app.accounts SET balance = balance + MONEY(?) WHERE account_number = ?;";

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
	public boolean accountWithdraw(Connection con, String accountNumber, double value) throws SQLException {
		String sql = "UPDATE bank_app.accounts SET balance = balance - MONEY(?) WHERE account_number = ?;";

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
	public boolean aBalDeposit(Connection con, String accountNumber, double value) throws SQLException {
		String sql = "UPDATE bank_app.accounts SET available_balance = available_balance + MONEY(?) WHERE account_number = ?;";

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
		String sql = "UPDATE bank_app.accounts SET available_balance = available_balance - MONEY(?) WHERE account_number = ?;";

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
	public boolean setActualEqualAvailable(Connection con, String accountNumber) throws SQLException {
		String sql = "UPDATE bank_app.accounts SET balance = available_balance WHERE account_number = ?";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, accountNumber);
		int count = ps.executeUpdate();

		if (count != 1) {
			throw new SQLException("Error: could not equalize balances for account: " + accountNumber);
		}

		return true;
	}
}
