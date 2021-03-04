package me.max.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.max.model.TransactionLog;

public class TransactionLogDAOImpl implements TransactionLogDAO {

	@Override
	public List<TransactionLog> getTransactionLogs(Connection con) throws SQLException {
		List<TransactionLog> result = new ArrayList<>();

		String sql = "SELECT * FROM bank_app.account_history;";

		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String account = rs.getString(2);
			String user = rs.getString(3);
			String desc = rs.getString(4);
			Date date = rs.getDate(5);
			TransactionLog e = new TransactionLog(account, user, desc, date);
			result.add(e);
		}

		return result;
	}

	@Override
	public boolean insertTransactionLog(Connection con, String account, String user, String desc) throws SQLException {
		String sql = "INSERT INTO bank_app.account_history (account_number, user_name, transaction_des) VALUES (?,?,?);";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, account);
		ps.setString(2, user);
		ps.setString(3, desc);
		
		int count = ps.executeUpdate();
		
		if(count != 1) {
			throw new SQLException("Error: Transaction Log could not be saved in database");
		};
		
		return true;
	}		
}
