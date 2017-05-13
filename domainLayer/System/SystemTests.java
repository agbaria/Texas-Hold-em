package System;

import static org.junit.Assert.*;

import org.junit.Test;

import user.UserStatus;

public class SystemTests {

	GameCenterInterface GCI=GameCenter.getInstance();
	

	
	@Test
	public void registerTests() {
		assertTrue(GCI.register("111111111", "12345780", "israel", "bla@bla.com"));

		assertFalse(GCI.register("111111111", "12345678", "isral", "bla@ba.com")); //same user cannot register twice
		assertFalse(GCI.register("222222222", "1234567", "israel", "bla@bla.com")); //the length of password must be at least 8 characters
		assertFalse(GCI.register("333333333", "12345678", "israel", "bla@blabla")); //invalid email (there must be '.' in valid address)
	
	}
	
	@Test
	public void loginTests() {
		GCI.register("222222222", "12345678", "israel", "bla@bla.com");
		assertTrue(GCI.login("222222222", "12345678"));
		
		assertEquals(GCI.getUser("222222222").getStatus(), UserStatus.CONNECTED); //check that the user in connected status after login
		assertFalse(GCI.login("222222222", "2525")); //wrong password for login
		assertFalse(GCI.login("121212", "2525")); //there is no such user
	}
	
	@Test
	public void logoutTests() {
		GCI.register("333", "12345678", "israel", "bla@bla.com");
		GCI.login("333", "12345678");
		
		assertNotEquals(GCI.getUser("333").getStatus(), UserStatus.DISCONNECTED); //the user connected now
		
		GCI.logout("333");
		assertEquals(GCI.getUser("333").getStatus(), UserStatus.DISCONNECTED); //the user "333" is now disconnected
		
	}
	
	@Test
	public void editUserPasswordTest() {
		GCI.register("444", "12345678", "israel", "bla@bla.com");
		
		assertEquals(GCI.getUser("444").getPassword(), "12345678"); //the user's password match
		GCI.editUserPassword("444", "12123434");

		assertNotEquals(GCI.getUser("444").getPassword(), "12345678"); //now after changing the user's password, it don't match with "12345678"
		assertEquals(GCI.getUser("444").getPassword(), "12123434"); //make sure that the password really changed
		
		assertFalse(GCI.editUserPassword("444", "1212")); //Can't change to short password
	}
	
	@Test
	public void editUserNameTest() {
		GCI.register("555", "12345678", "israel", "bla@bla.com");
		
		assertEquals(GCI.getUser("555").getName(), "israel"); //the user's name match
		GCI.editUserName("555", "roee");

		assertNotEquals(GCI.getUser("555").getName(), "israel"); //now after changing the user's name, it don't match with "israel"
		assertEquals(GCI.getUser("555").getName(), "roee"); //make sure that the name really changed
	}
	
	@Test
	public void editUserEmailTest() {
		GCI.register("666", "12345678", "israel", "bla@bla.com");
		
		assertEquals(GCI.getUser("666").getEmail(), "bla@bla.com"); //the user's email match
		GCI.editUserEmail("666", "yesrael@gmail.com");

		assertNotEquals(GCI.getUser("666").getEmail(), "bla@bla.com"); //now after changing the user's email, it don't match with "bla@bla.com"
		assertEquals(GCI.getUser("666").getEmail(), "yesrael@gmail.com"); //make sure that the email really changed
		
		assertFalse(GCI.editUserEmail("666", "yes.gmail.com")); //Can't change to invalid email address
		
	}

}
