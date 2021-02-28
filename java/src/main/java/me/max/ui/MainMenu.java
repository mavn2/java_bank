package me.max.ui;

// Run on application launch, pass to login or create account
public class MainMenu implements Menu {
	public void display() {
		// User input is tracked in this integer var
		int choice = 0;

		// Following will print/execute as long as exit not selected
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

		} while (choice != 1);
	}
}
