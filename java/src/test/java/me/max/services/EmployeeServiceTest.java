package me.max.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import me.max.dao.AccountDAO;
import me.max.dao.UserDAO;
import me.max.model.Account;
import me.max.model.User;

public class EmployeeServiceTest {

	public EmployeeService employeeService;
	public static AccountDAO accountDAO;
	public static UserDAO userDAO;
	public User testUser;
	public User testUser2;
	public Account testAccount;
	public Account testAccount2;
	public static List<Account> result;
	public static List<User> resultU;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		result = new ArrayList<>();
		result.add(new Account("test123", "testUser", "Checking", "Pending", 100d, 100d));
		result.add(new Account("test1234", "testUser", "Savings", "Pending", 100d, 100d));

		resultU = new ArrayList<>();
		resultU.add(new User("testUser", "Tester", "Testerson", "0123456789", 2));
		resultU.add(new User("testUser2", "Second", "Test", "0123456789", 2));

		accountDAO = mock(AccountDAO.class);
		userDAO = mock(UserDAO.class);

		when(accountDAO.getAccountsByStatus(any(Connection.class), eq("Pending"))).thenReturn(result);
		when(accountDAO.approveAccount(any(Connection.class), eq("test123"))).thenReturn(true);
		when(userDAO.getAllUsersByType(any(Connection.class), eq(2))).thenReturn(resultU);
		when(accountDAO.getAccountsByOwner(any(Connection.class), eq("testUser"))).thenReturn(result);
	}

	@AfterClass
	public static void tearDownAfterClass() {
	}

	@Before
	public void setUp() throws Exception {
		employeeService = new EmployeeService(accountDAO, userDAO);
		testUser = new User("testUser", "Tester", "Testerson", "0123456789", 2);
		testUser2 = new User("testUser2", "Second", "Test", "0123456789", 2);
		testAccount = new Account("test123", "testUser", "Checking", "Active", 100d, 100d);
		testAccount2 = new Account("test1234", "testUser", "Savings", "Active", 100d, 100d);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testgetListOfCustomers() throws Exception {

		List<User> actual = employeeService.getListOfCustomers();
		List<User> expected = resultU;

		assertEquals(expected, actual);
	}

	@Test
	public void testApproveAccount() throws Exception {

		boolean actual = employeeService.approvePendingAccount("test123");
		boolean expected = true;

		assertEquals(expected, actual);
	}

	@Test
	public void testViewUserAccounts() throws Exception {
		List<Account> actual = employeeService.getCustomerAccounts("testUser");
		List<Account> expected = result;

		assertEquals(expected, actual);
	}
}
