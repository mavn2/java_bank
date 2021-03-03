package me.max.main;

import me.max.model.User;
import me.max.ui.MainMenu;
import me.max.ui.Menu;

public class Application {

	public static User currentUser;

	public static void main(String[] args) {		
		// Create new MainMenu object on app launch
		Menu menu = new MainMenu();
		menu.display();

		// Close scanner once user has exited menu(es)
		Menu.sc.close();
	}

}
