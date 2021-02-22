package me.max.ui;

import me.max.model.User;

public class UserMenu implements Menu {
	
	//Can hold data for user accessing app
	public User user;
	
	//Custom constructor takes a user object as parameter,
	// ensures access to relevant information
	public UserMenu(User u) {
		this.user = u;
	}
	
	//Temp no args
	public UserMenu() {
	}
	
	@Override
	public void display() {
		
		System.out.println("request account?: quit?");
			
	}

}
