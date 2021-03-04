package me.max.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import me.max.model.Account;
import me.max.model.Transfer;

public interface AccountDAO {
	Account insertAccount(Connection con, String username, double startingBalance, String type) throws SQLException;
	boolean pairAccountAndUser(Connection con, String username, String accountNumber) throws SQLException;
	List<Account> getAllAcounts(Connection con) throws SQLException;
	List<Account> getAccountsByStatus(Connection con, String status) throws SQLException;
	List<Account> getAccountsByOwner(Connection con, String userName) throws SQLException;
	Account getAccountById(Connection con, String accountNumber) throws SQLException;
	double getBalanceById(Connection con, String accountNumber) throws SQLException;
	double getAvailableById(Connection con, String accountNumber) throws SQLException;
	boolean approveAccount(Connection con, String accountNumber) throws SQLException;
	boolean accountDeposit(Connection con, String accountNumber, double value) throws SQLException;
	boolean accountWithdraw(Connection con, String accountNumber, double value) throws SQLException;
	boolean aBalDeposit(Connection con, String accountNumber, double value) throws SQLException;
	boolean aBalWithdraw(Connection con, String accountNumber, double value) throws SQLException;
	int startTransfer(Connection con, String userFrom, String accountFrom, String userTo, String accountTo, double amount) throws SQLException;
	int endTransfer(Connection con, int id, String userFrom, String userTo, String accountTo, double amount, boolean approval) throws SQLException;
	List<Transfer> getPendingTransfersForUser(Connection con, String username) throws SQLException;
}
