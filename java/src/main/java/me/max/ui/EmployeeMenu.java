package me.max.ui;

import me.max.model.User;

public class EmployeeMenu implements Menu {
	private User u;
	
	public void display() {
		System.out.println(this.u);
	}
	
	public EmployeeMenu(User user) {
		u = user;
	}
}
