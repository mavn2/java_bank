package me.max.services;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import me.max.dao.UserDAO;
import me.max.exceptions.UserNotFoundException;
import me.max.model.User;

public class UserServiceTest {
	//Static vars to be written when test runs
	public UserService userService;
	public static UserDAO userDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		userDAO = mock(UserDAO.class);
		Connection con = any(Connection.class);
		when(userDAO.getUserByUsername(con, eq("testingTest"))).thenReturn(new User("testingTest", "test", "complete", "1234567890"));
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
	}
	
	@Before 
	public void setUp() throws Exception {
		userService = new UserService(userDAO);
	}
	
	@Test
	public void test() throws Exception{
		User actual = userService.getUserByUsername("testingTest");
		User expected = new User("testingTest", "test", "complete", "1234567890");
		assertEquals(expected, actual);
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testInvalidUserName() throws SQLException, UserNotFoundException {
		@SuppressWarnings("unused")
		User actual = userService.getUserByUsername("shouldFail");
	}
	
	@Test
	public void testUserCreation() throws SQLException{
		User actual = userService.createNewUser("test123", "John", "Doe", "0123456789");
		User expected = new User("test123", "John", "Doe", "0123456789");
		assertEquals(expected, actual);
	}
	
	@Test(expected = SQLException.class)
	public void testUserCreationFailure() throws SQLException{
		userService.createNewUser("this", "numbers", "wrong", "waymorethanthetendigitsacleanedphonenumberwouldadd");
	}
}
