package me.max.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import me.max.dao.ValidationDAO;
import me.max.exceptions.UserCreationException;

public class ValidationServiceTest {
	public ValidationService validationService;
	public static ValidationDAO validationDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		validationDAO = mock(ValidationDAO.class);
		when(validationDAO.validateUserName(any(Connection.class), eq("newUser"))).thenReturn(false);
		when(validationDAO.validateUserName(any(Connection.class), eq("Admin"))).thenReturn(true);
	}

	@AfterClass
	public static void tearDownAfterClass() {
	}

	@Before
	public void setUp() throws Exception {
		validationService = new ValidationService(validationDAO);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNewUniqueUsername() throws Exception {
		String actual = validationService.checkIfUniqueUsername("newUser");
		String expected = "newUser";
		assertEquals(expected, actual);
	}
	
	@Test(expected = UserCreationException.class)
	public void testNewDuplicateUsername() throws Exception {
		@SuppressWarnings("unused")
		String actual = validationService.checkIfUniqueUsername("Admin");
	}
	
	@Test
	public void testCorrectNewPasswords() throws Exception{
		String actual = validationService.checkIfMatchingPasswords("String", "String");
		String expected = "String";
		assertEquals(expected, actual);
	}
	
	@Test(expected = UserCreationException.class)
	public void testUnmatchedNewPasswords() throws Exception{
		@SuppressWarnings("unused")
		String actual = validationService.checkIfMatchingPasswords("OneThing", "Another");
	}
}
