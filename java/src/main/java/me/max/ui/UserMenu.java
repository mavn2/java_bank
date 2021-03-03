package me.max.ui;

import me.max.model.User;

public class UserMenu implements Menu {

	// Can hold data for user accessing app
	private User u;

	// Custom constructor takes a user object as parameter,
	// ensures access to relevant information
	public UserMenu(User user) {
		u = user;
	}

	@Override
	public void display() {

		int choice = 0;

		do {
			System.out.println("===============");
			System.out.println("USER MENU");
			System.out.println("===============");
			System.out.println("1.) Go back");
			System.out.println("2.) Request an account");
			
			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
			}
			
			switch(choice) {
			case 1: 
				break;
			case 2:
				requestAccount();
			}
		} while (choice != 1);

	}
	
	private void requestAccount(){
		//logic
	}

}
