package me.max.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.sql.Connection;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import me.max.dao.AccountDAO;
import me.max.exceptions.AccountCreationException;
import me.max.exceptions.TransactionException;
import me.max.model.Account;
import me.max.model.User;

public class CustomerServiceTest {

	public CustomerService customerService;
	public static AccountDAO accountDAO;
	public User testUser;
	public Account testAccount;
	public Account testAccount2;
	public static List<Account> result;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Set up array list to be 'returned' by mock DAO below
		result = new ArrayList<>();
		result.add(new Account("test123", "testUser", "Checking", "Active", 100d, 100d));
		result.add(new Account("test1234", "testUser", "Savings", "Active", 100d, 100d));

		accountDAO = mock(AccountDAO.class);
		when(accountDAO.insertAccount(any(Connection.class), eq("testUser"), eq(100d), eq("Checking")))
				.thenReturn(new Account("test123", "testUser", "Checking", "Active", 100d, 100d));
		when(accountDAO.getAccountById(any(Connection.class), eq("test123")))
				.thenReturn(new Account("test123", "testUser", "Checking", "Active", 100d, 100d));
		when(accountDAO.getAccountsByOwner(any(Connection.class), eq("testUser"))).thenReturn(result);
		when(accountDAO.aBalDeposit(any(Connection.class), eq("test123"), eq(10d))).thenReturn(true);
		when(accountDAO.accountDeposit(any(Connection.class), eq("test123"), eq(10d))).thenReturn(true);
		when(accountDAO.aBalWithdraw(any(Connection.class), eq("test123"), eq(10d))).thenReturn(true);
		when(accountDAO.accountWithdraw(any(Connection.class), eq("test123"), eq(10d))).thenReturn(true);
		when(accountDAO.aBalDeposit(any(Connection.class), eq("test1234"), eq(10d))).thenReturn(true);
		when(accountDAO.accountDeposit(any(Connection.class), eq("test1234"), eq(10d))).thenReturn(true);
	}

	@AfterClass
	public static void tearDownAfterClass() {
	}

	@Before
	public void setUp() throws Exception {
		customerService = new CustomerService(accountDAO);
		testUser = new User("testUser", "Tester", "Testerson", "0123456789", 2);
		testAccount = new Account("test123", "testUser", "Checking", "Active", 100d, 100d);
		testAccount2 = new Account("test1234", "testUser", "Savings", "Active", 100d, 100d);
	}

	@After
	public void tearDown() throws Exception {
		testAccount = null;
		testAccount2 = null;
	}

	@Test
	public void testValidAccountCreation() throws Exception {
		Account actual = customerService.requestNewAccount(testUser, 100d, 1);
		Account expected = new Account("test123", "testUser", "Checking", "Active", 100d, 100d);
		assertEquals(expected, actual);
	}

	@Test(expected = AccountCreationException.class)
	public void testInvalidAccountCreation() throws Exception {
		@SuppressWarnings("unused")
		Account actual = customerService.requestNewAccount(testUser, -100d, 1);
	}

	@Test
	public void testGetAccountList() throws Exception {
		List<Account> actual = customerService.getUserAccounts(testUser);
		List<Account> expected = result;
		assertEquals(expected, actual);
	}

	@Test
	public void testValidDeposit() throws Exception {
		Account actual = customerService.transferIntoAccount(testAccount, testUser, 10d);
		Account expected = new Account("test123", "testUser", "Checking", "Active", 110d, 110d);
		assertEquals(expected, actual);
	}

	@Test(expected = TransactionException.class)
	public void testInvalidDeposit() throws Exception {
		@SuppressWarnings("unused")
		Account actual = customerService.transferIntoAccount(testAccount, testUser, -1000d);
	}

	@Test
	public void testValidWithdrawal() throws Exception {
		Account actual = customerService.withdrawFromAccount(testAccount, testUser, 10d);
		Account expected = new Account("test123", "testUser", "Checking", "Active", 90d, 90d);
		assertEquals(expected, actual);
	}

	@Test (expected = TransactionException.class)
	public void testInvalidWithdrawl() throws Exception {
		@SuppressWarnings("unused")
		Account actual = customerService.withdrawFromAccount(testAccount, testUser, -1000d);
	}

	@Test
	public void testValidInternalTransfer() throws Exception {
		customerService.transferBetweenAccounts(testAccount, testAccount2, testUser, 10d);
		Account actual = testAccount;
		Account actual2 = testAccount2;
		Account expected = new Account("test123", "testUser", "Checking", "Active", 90d, 90d);
		Account expected2  = new Account("test1234", "testUser", "Savings", "Active", 110d, 110d);
		assertEquals(expected2, actual2);
		assertEquals(expected, actual);
	}

	@Test(expected = TransactionException.class)
	public void testInvalidInternalTransfer() throws Exception {
		customerService.transferBetweenAccounts(testAccount, testAccount2, testUser, 1000d);
	}

}
