package me.max.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.max.exceptions.TransactionException;
import me.max.model.Account;
import me.max.model.Customer;
import me.max.model.Transfer;
import me.max.model.User;
import me.max.services.CustomerService;

public class CustomerMenu implements Menu {
	private User u;
	List<Account> accounts;
	CustomerService customerService;

	// Custom no args constructor
	public CustomerMenu(User user) {
		u = user;
		this.customerService = new CustomerService();
	}

	public void display() {
		try {
			// Get Customer object for user
			u = customerService.getCustomerAccount(u.getUsername());
			// Can downcast u, use Customer methods for easy access to accounts
			accounts = ((Customer) u).getAccounts();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("===============");
		System.out.println(" * MAIN MENU * ");
		System.out.println("===============");
		System.out.println();
		System.out.println("Welcome, " + u.getFirstName());
		System.out.println();
		System.out.println("1.) Log Out and Quit");
		System.out.println("2.) View my Accounts");
		System.out.println("3.) Withdraw from Account");
		System.out.println("4.) Deposit in Account");
		System.out.println("5.) Transfer to another User");
		System.out.println("6.) Check for pending transfers");

		int choice = 0;

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
				viewMyAccounts();
				break;
			case 3:
				withdrawOwnAccount();
				break;
			case 4:
				depositOwnAccount();
				break;
			case 5:
				postTransfer();
				break;
			case 6:
				checkTransfers();
				break;
			default:
				System.out.println("Please enter a valid selection.");
			}

		} while (choice != 1);
	}

	private void viewMyAccounts() {
		int i = 1;
		System.out.println("#) Type | Available | Current | Number ");
		for (Account e : accounts) {
			System.out.println(i + ".) " + e.getAccountType() + " | " + e.getAvailableBalance() + " | "
					+ e.getCurrentBalance() + " | " + e.getAccountNumber());
			i++;
		}
	}

	private void withdrawOwnAccount() {
		viewMyAccounts();
		System.out.println("Select account to withdraw from:");

		String status;
		int choice;
		double amount;

		try {
			choice = Integer.parseInt(Menu.sc.nextLine());

			if (choice <= accounts.size() && choice != 0) {
				// Subtract one from choice to get index value
				int index = choice - 1;
				// Get account for transfer, number for user feedback
				Account choiceAccount = (accounts.get(index));
				String choiceNum = choiceAccount.getAccountNumber();
				// Get balance values for updates to client side object
				double balance = choiceAccount.getCurrentBalance();
				double aBalance = choiceAccount.getAvailableBalance();

				System.out.println("Enter withdrawal amount:");
				amount = Double.parseDouble(Menu.sc.nextLine());

				System.out.println("Withdraw " + amount + " from " + choiceNum + "?");
				System.out.println("Enter y/n:");

				do {
					status = sc.nextLine();
					switch (status) {
					case "y":
						System.out.println("Withdrawal confirmed");
						customerService.withdrawFromAccount(choiceAccount, u, amount);
						choiceAccount.setAvailableBalance(aBalance - amount);
						choiceAccount.setCurrentBalance(balance - amount);
						break;
					case "n":
						System.out.println("Withdrawal canceled.");
						break;
					default:
						System.out.println("Please enter y or n:");
					}
				} while (!status.equals("y") && !status.equals("n"));
			}
		} catch (NumberFormatException e) {
			System.out.println("Please select account based on list number above.");
		} catch (TransactionException | SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void depositOwnAccount() {
		viewMyAccounts();
		System.out.println("Select account for deposit:");

		String status;
		int choice;
		double amount;

		try {
			choice = Integer.parseInt(Menu.sc.nextLine());

			if (choice <= accounts.size() && choice != 0) {
				// Subtract one from choice to get index value
				int index = choice - 1;
				// Get account for transfer, number for user feedback
				Account choiceAccount = (accounts.get(index));
				String choiceNum = choiceAccount.getAccountNumber();
				// Get balance values for updates to client side object
				double balance = choiceAccount.getCurrentBalance();
				double aBalance = choiceAccount.getAvailableBalance();

				System.out.println("Enter deposit amount:");
				amount = Double.parseDouble(Menu.sc.nextLine());

				System.out.println("Deposit " + amount + " in " + choiceNum + "?");
				System.out.println("Enter y/n:");

				do {
					status = sc.nextLine();
					switch (status) {
					case "y":
						System.out.println("Deposit confirmed.");
						// Send update to database
						customerService.transferIntoAccount(choiceAccount, u, amount);
						// Update client side object
						choiceAccount.setAvailableBalance(aBalance + amount);
						choiceAccount.setCurrentBalance(balance + amount);
						break;
					case "n":
						System.out.println("Deposit canceled.");
						break;
					default:
						System.out.println("Please enter y or n.");
					}
				} while (!status.equals("y") && !status.equals("n"));
			}
		} catch (NumberFormatException e) {
			System.out.println("Please select account based on list number above.");
		} catch (TransactionException | SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void postTransfer() {
		System.out.println("Enter a user to recieve the transfer:");
		String username = Menu.sc.nextLine();

		System.out.println("Enter an account number belonging to that user:");
		String account = Menu.sc.nextLine();

		System.out.println(
				"Enter an amount to deposit - this will not leave your account unless the transfer is accepted.");
		double amount = Double.parseDouble(Menu.sc.nextLine());

		System.out.println("Select an account to transfer from, or 0 to quit this process.");

		viewMyAccounts();
		String status;
		Account choiceAccount = null;

		int choice;
		try {
			choice = Integer.parseInt(Menu.sc.nextLine());

			if (choice <= accounts.size() && choice != 0) {
				// Subtract one from choice to get index value
				int index = choice - 1;
				// Get account for transfer, number for user feedback
				choiceAccount = (accounts.get(index));

				System.out.println("Confirm transfer of " + amount + " to User " + username + " from account "
						+ choiceAccount.getAccountNumber() + "?");
				System.out.println("Enter y/n");
				do {
					status = sc.nextLine();
					switch (status) {
					case "y":
						try {
							customerService.startTransfer(u, choiceAccount, username, account, amount);
						} catch (SQLException ex) {
							System.out.println(ex.getMessage());
						}
						System.out.println("Transfer submitted!");
						break;
					case "n":
						System.out.println("Transfer cancelled.");
						break;
					default:
						System.out.println("Please enter y or n");
					}
				} while (!status.equals("y") && !status.equals("n"));
			}
		} catch (NumberFormatException e) {
			System.out.println("Please select account based on list number above.");
		}
	}

	private void checkTransfers() {
		List<Transfer> transfers = new ArrayList<>();
		Transfer t = null;
		try {
			transfers = customerService.getPendingTransfers(u);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		if (!transfers.isEmpty()) {
			System.out.println("#) From | Amount | To");
			int i = 1;

			for (Transfer e : transfers) {
				System.out.println(i + ".) " + e.getuFrom() + " | " + e.getAmount() + " | " + e.getaTo());
			}
			String status;
			Account a = null;

			do {
				int choice;
				System.out.println("Enter a transfer's number to approve or decline it, or 0 to go back.");
				try {
					choice = Integer.parseInt(Menu.sc.nextLine());

					if (choice <= accounts.size() && choice != 0) {
						// Subtract one from choice to get index value
						int index = choice - 1;
						// Select transfer from array
						t = transfers.get(index);
						int i2 = 0;

						for (Account account : accounts) {
							String num = t.getaTo();
							if (account.getAccountNumber().contentEquals(num)) {
								a = accounts.get(i2);
							}
							i2++;
						}
					}
				} catch (NumberFormatException e2) {
				}
				System.out.println("Approve or Decline? (y/n)");
				status = sc.nextLine();
			} while (!status.equals("y") && !status.equals("n"));

			switch (status) {
			case "y":
				try {
					customerService.endTransfer(a, t, true);
				} catch (SQLException e1) {
					System.out.println(e1.getMessage());
				}
				System.out.println("Transfer complete!");
				break;
			case "n":
				try {
					customerService.endTransfer(a, t, false);
				} catch (SQLException e1) {
					System.out.println(e1.getMessage());
				}
				System.out.println("Transfer declined.");
				break;
			}
		} else {
			System.out.println("No pending transfers.");
		}
	}
}
