package me.max.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import me.max.dao.UserDAO;
import me.max.exceptions.UserCreationException;
import me.max.exceptions.UserNotFoundException;
import me.max.model.User;

public class UserServiceTest {
	//Static vars to be written when test runs
	public UserService userService;
	public static UserDAO userDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		userDAO = mock(UserDAO.class);
		when(userDAO.getUserByUsername(any(Connection.class), eq("testingTest"))).thenReturn(new User("testingTest", "test", "complete", "1234567890"));
		when(userDAO.insertNewUser(any(Connection.class), eq("test123"), eq("password"), eq("John"), eq("Doe"), eq("0123456789"))).thenReturn(new User("test123", "John", "Doe", "0123456789"));
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
	}
	
	@Before 
	public void setUp() throws Exception {
		userService = new UserService(userDAO);
	}
	
	@After
	public void tearDown() throws Exception {
		userService = null;
	};
	
	@Test
	public void test() throws Exception{
		User actual = userService.getUserByUsername("testingTest");
		User expected = new User("testingTest", "test", "complete", "1234567890");
		assertEquals(expected, actual);
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testInvalidUserName() throws Exception {
		@SuppressWarnings("unused")
		User actual = userService.getUserByUsername("shouldFail");
	}
	
	@Test
	public void testUserCreation() throws Exception{
		User actual = userService.createNewUser("test123", "password", "John", "Doe", "0123456789");
		User expected = new User("test123", "John", "Doe", "0123456789");
		assertEquals(expected, actual);
	}
	
	@Test(expected = UserCreationException.class)
	public void testUserCreationFailure() throws Exception {
		User actual = userService.createNewUser("this", "number", "is", "wrong", "waymorethanthetendigitsacleanedphonenumberwouldadd");

	}
}