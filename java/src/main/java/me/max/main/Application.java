package me.max.main;

import java.sql.SQLException;

import me.max.util.ConnectionUtil;

public class Application {

	public static void main(String[] args) {
		//Testing
		try {
			System.out.println(ConnectionUtil.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
