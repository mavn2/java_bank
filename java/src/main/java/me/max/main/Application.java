package me.max.main;

import java.sql.SQLException;

import me.max.ui.LaunchMenu;
import me.max.ui.Menu;
import me.max.util.ConnectionUtil;

public class Application {

	public static void main(String[] args) {
		// Testing
		try {
			System.out.println(ConnectionUtil.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Init. a LaunchMenu object
		Menu menu = new LaunchMenu();
		// Display launch menu on app launch(!)
		menu.display();

		// Close scanner once user has exited menu(es)
		Menu.sc.close();
	}

}
