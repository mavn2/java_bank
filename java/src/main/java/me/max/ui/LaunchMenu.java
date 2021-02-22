package me.max.ui;

// Run on application launch, pass to login or create account
public class LaunchMenu implements Menu {
	public void display() {
		// User input is tracked in this integer var
		int choice = 0;

		// Following will print/execute as long as exit not selected
		do {
			System.out.println("===============");
			System.out.println("Welcome To Bank");
			System.out.println("===============");
			System.out.println("*Please choose an option to continue*");
			System.out.println("1.) Sign in to Account.");
			System.out.println("2.) Create New Account.");
			System.out.println("3.) Exit Application.");

			// Listen for user input/assign to choice var,
			// throws exception if invalid
			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
			}

			// Execute code based on user choice
			switch (choice) {
			case 1:
				Menu loginMenu = new LoginMenu();
				loginMenu.display();
				break;
			case 2:
				System.out.println("create account");
				break;
			case 3:
				System.out.println("Thank you for using Bank!");
				break;
			default:
				System.out.println("Please enter a valid choice or number.");
			}

		} while (choice != 3);
	}
}
