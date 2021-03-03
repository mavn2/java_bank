package me.max.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.max.model.Account;
import me.max.model.Customer;

public class CustomerDAOImpl implements CustomerDAO {

	@Override
	public Customer getCustomerByUsername(Connection con, String username) throws SQLException {
		Customer result = null;
		List<Account> accountResult = new ArrayList<>(null);

		String sql = "SELECT c.*, a.* FROM bank_app.users c JOIN bank_app.accounts a ON c.user_name = a.account_owner WHERE c.user_name = ?;";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();

		// Check for rows in results, use first to set parent class fields if founds
		if (rs.next()) {
			result = new Customer(rs.getString("user_name"), rs.getString("first_name"), rs.getString("last_name"),
					rs.getString("phone_number"), rs.getInt("phone_number"));
		}

		// Return cursor to original position
		rs.beforeFirst();

		// Iterate over all rows in result set, creating an Account object for each
		// And adding those to the
		while (rs.next()) {
			Account entry = new Account(rs.getString("account_number"), rs.getString("account_owner"),
					rs.getString("account_type"), rs.getString("account_status"), rs.getDouble("balance"),
					rs.getDouble("available_balance"));
			accountResult.add(entry);
		}
		
		// Set list with all accounts to customer object
		result.setAccounts(accountResult);

		return result;
	}
}
