package me.max.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.max.exceptions.AccountNotFoundException;
import me.max.model.Account;
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

		while (choice != 1) {
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

			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
			}

			switch (choice) {
			case 1:
				break;
			case 2:
				viewAndApprove();
				break;
			case 3:
				//viewCustomerAccounts();
				break;
			case 4: 
				//viewTransactionLog();
				break;
			default:
				System.out.println("Please enter a valid selection");
			}
		}
	}
	
	private void viewAndApprove() {
		// Retrieve arrayList w/ pending accounts
		List <Account> accounts = new ArrayList<>();
		
		try{
			accounts = employeeService.getPendingAccounts();
			
		} catch (SQLException | AccountNotFoundException e) {
			System.out.println(e.getMessage());
		}	
		
		if(!accounts.isEmpty()) {
			System.out.println("#) Acc. Owner | Proposed Bal. | Type");
			
			for(Account e : accounts) {
				int i = 1;
				System.out.println(i + ".) " + e.getAccountOwner() + " | " + e.getCurrentBalance() + " | " + e.getAccountType());
				i++;
			}
			
			System.out.println("Enter the number of an account to approve, or enter 0 to go back");
			
//			String status;
//			int choice;
//			do {
//				try {
//					choice = Integer.parseInt(Menu.sc.nextLine());
//					
//				} catch (NumberFormatException e) {
//				}
//			} while (status.equals("y"));
		}
	}
}
