package me.max.ui;

import me.max.model.User;

public class CustomerMenu implements Menu {
	private User u;
	
	public void display () {
		System.out.println(this.u);
	}
	
	//Custom no args constructor
	public CustomerMenu(User user){
		u = user;
	}
}
