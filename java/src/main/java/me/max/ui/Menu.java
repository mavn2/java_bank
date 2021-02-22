package me.max.ui;

import java.util.Scanner;

public interface Menu {
	// Instantiate a scanner for use in menu classes
	public static final Scanner sc = new Scanner(System.in);

	// Every menu class should include a class-specific display method
	void display();
}
