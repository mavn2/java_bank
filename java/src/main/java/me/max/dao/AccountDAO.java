package me.max.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import me.max.model.Account;

public interface AccountDAO {
	Account insertAccount(Connection con, String username, double startingBalance, String type) throws SQLException;
	boolean pairAccountAndUser(Connection con, String username, String accountNumber) throws SQLException;
	Account getAccountById(Connection con, String accountNumber) throws SQLException;
	double getBalanceById(Connection con, String accountNumber) throws SQLException;
	double getAvailableById(Connection con, String accountNumber) throws SQLException;
	List<Account> getAccountsByOwner(Connection con, String userName) throws SQLException;
	boolean approveAccount(Connection con, String accountNumber) throws SQLException;
	boolean accountDeposit(Connection con, String accountNumber, double value) throws SQLException;
	boolean accountWithdraw(Connection con, String accountNumber, double value) throws SQLException;
	boolean aBalDeposit(Connection con, String accountNumber, double value) throws SQLException;
	boolean aBalWithdraw(Connection con, String accountNumber, double value) throws SQLException;
	boolean setActualEqualAvailable(Connection con, String accountNumber) throws SQLException;
}
