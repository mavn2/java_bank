package me.max.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import me.max.model.TransactionLog;

public interface TransactionLogDAO {
	List<TransactionLog> getTransactionLogs(Connection con) throws SQLException;
	boolean  insertTransactionLog(Connection con, String account, String user, String desc) throws SQLException;
}
