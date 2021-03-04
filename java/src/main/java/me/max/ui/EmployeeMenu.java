package me.max.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.max.exceptions.AccountNotFoundException;
import me.max.model.Account;
import me.max.model.TransactionLog;
import me.max.model.User;
import me.max.services.EmployeeService;

public class EmployeeMenu implements Menu {
	private User u;
	EmployeeService employeeService;

	public EmployeeMenu(User user) {
		u = user;
		employeeService = new EmployeeService();
	}

	public void display() {
		int choice = 0;

		System.out.println("===============");
		System.out.println(" * MAIN MENU * ");
		System.out.println("===============");
		System.out.println();
		System.out.println("Welcome, " + u.getFirstName());
		System.out.println();
		System.out.println("1.) Log Out and Quit");
		System.out.println("2.) View/Approve Pending Accounts");
		System.out.println("3.) View Customer's accounts");
		System.out.println("4.) View Transaction Log");

		do {
			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
				choice = 0;
			}

			switch (choice) {
			case 1:
				System.out.println("Shutting down.");
				break;
			case 2:
				viewAndApprove();
				break;
			case 3:
				viewAccountsForCustomer();
				break;
			case 4:
				viewTransactionLog();
				break;
			default:
				System.out.println("Please enter a valid selection");
			}

		} while (choice != 1);
	}

	private void viewAndApprove() {
		// Retrieve arrayList w/ pending accounts
		List<Account> accounts = new ArrayList<>();

		try {
			accounts = employeeService.getPendingAccounts();

		} catch (SQLException | AccountNotFoundException e) {
			System.out.println(e.getMessage());
		}

		if (!accounts.isEmpty()) {
			System.out.println("#) Acc. Owner | Proposed Bal. | Type");

			int i = 1;

			for (Account e : accounts) {
				System.out.println(
						i + ".) " + e.getAccountOwner() + " | " + e.getCurrentBalance() + " | " + e.getAccountType());
				i++;
			}

			System.out.println("Enter the number of an account to approve, or enter 0 to go back");

			String status;
			do {
				int choice;

				try {
					choice = Integer.parseInt(Menu.sc.nextLine());

					if (choice <= accounts.size() && choice != 0) {
						// Subtract one from choice to get index value
						int index = choice - 1;
						// Get account number and owner id for selection
						String choiceNum = accounts.get(index).getAccountNumber();
						String choiceOwn = accounts.get(index).getAccountOwner();

						// Set account to active, and user to Customer
						try {
							employeeService.approvePendingAccount(choiceNum);
							employeeService.promoteUser(choiceOwn);

						} catch (SQLException e1) {
							System.out.println(e1.getMessage());
						}
					}
				} catch (NumberFormatException e) {
				}
				System.out.println("Approve another? (y/n)");
				status = sc.nextLine();
			} while (status.equals("y"));
		}
	}

	private void viewAccountsForCustomer() {
		System.out.println("Enter Username to view accounts, or BACK to go back");

		String input = Menu.sc.nextLine();
		List<Account> result;

		try {
			if (input != "BACK") {
				result = employeeService.getCustomerAccounts(input);

				System.out.println("Accounts for User " + input);
				System.out.println("Acc. Number | Status |  Bal.  | Available | Type ");
				for (Account e : result) {
					System.out.println(e.getAccountNumber() + " | " + e.getAccountStatus() + " | "
							+ e.getCurrentBalance() + " | " + e.getAvailableBalance() + " | " + e.getAccountType());
				}
				System.out.println("Press enter to continue");
				sc.nextLine();
			}
		} catch (SQLException | AccountNotFoundException e) {
			e.getMessage();
		}
	}

	private void viewTransactionLog() {
		try {
			List<TransactionLog> logs = employeeService.getHistory();
			
			System.out.println("Account | User | Description | Date ");
				
			for (TransactionLog e : logs) {
				String date = e.getDate().toString();
				System.out.println(e.getAccountNumber() + " | " + e.getUsername() + " | " + e.getDesc() + " | " + date);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	};

}
