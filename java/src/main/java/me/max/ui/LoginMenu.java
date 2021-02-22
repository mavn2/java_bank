package me.max.ui;

public class LoginMenu implements Menu{

	@Override
	public void display() {
		int step = 0;
		
		String userName;

		do {	
		System.out.println("================");
		System.out.println("Sign In");
		System.out.println("================");
		System.out.println("Please Enter Your Username");
		// need a back option-q or 1?
		// will need custom exception or other user feedback/error msg
		userName = sc.next();
		System.out.println(userName);
		} while (step == 0);
	}
	
}
