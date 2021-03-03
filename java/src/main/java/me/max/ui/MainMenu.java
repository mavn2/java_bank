package me.max.ui;

import me.max.main.Application;
import me.max.model.User;

// Run on application launch, pass to login or create account
public class MainMenu implements Menu {
	
	public void display() {
		while(Application.currentUser == null) {
			displayDefaultMenu();
		}
		
		displayActiveMenu(Application.currentUser);
	}

	// display default menu
	private void displayDefaultMenu() {
		// User input is tracked in this integer var
		int choice = 0;
		do {
			System.out.println("===============");
			System.out.println("Welcome To Bank");
			System.out.println("===============");
			System.out.println("*Please choose an option to continue*");
			System.out.println("1.) Exit Application");
			System.out.println("2.) Create New Account");
			System.out.println("3.) Sign in to account");

			// Listen for user input/assign to choice var,
			// throws exception if invalid
			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
				//Default switch case handles error message here
			}

			// Execute code based on user choice
			switch (choice) {
			case 1:
				System.out.println("Shutting down");
				break;
			case 2:
				Menu newMenu = new UserCreationMenu();
				newMenu.display();
				break;
			case 3:
				Menu loginMenu = new LoginMenu();
				loginMenu.display();
				break;
			default:
				System.out.println("Please enter a valid selection.");
			}
			//Break out of loop after execution, allows currentUser to be checked
			break;
			//Will run again if needed
		} while (choice != 1 && Application.currentUser == null);
	}

	// display tailored menu
	private void displayActiveMenu(User user) {
		int userType = user.getType();
		switch(userType) {
		case 1: 
			Menu userMenu = new UserMenu(user);
			userMenu.display();
			break;
		case 2:
			Menu customerMenu = new CustomerMenu(user);
			customerMenu.display();
			break;
		case 3:
			Menu employeeMenu= new EmployeeMenu(user);
			employeeMenu.display();
		}	
	}
}
