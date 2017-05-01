package System;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Game.Game;
import Game.Player;
import user.User;

public class GameCenter implements GmaeCenterInterface{
   private  ConcurrentLinkedQueue<User> users;
   private ConcurrentLinkedQueue<Game> games;
   private static GmaeCenterInterface singleton = new GameCenter( );
   
   public static GmaeCenterInterface getInstance( ) {
	      return singleton;
	   }
   
   private GameCenter(){
	   users = new ConcurrentLinkedQueue<User>();
	   games= new ConcurrentLinkedQueue<Game>();
   }
   
   /**
    * 
    * @param email
    * @return true if email is valid email address
    */
   private boolean isValidEmailAddress(String email)
   {
	   Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
       Matcher mat = pattern.matcher(email);
       return mat.matches();
   }
   
  
  /**
   * this function get all the details of unregistered user, check them under the game policy, if there is'nt problem with one or more of the details make a new user and add him to the system
   *  @return true if the user can register to the system, else return false;
   */
   public void register(String ID, String password, String name, String email) throws Exception
   {
	   User newUser;

	   for (User usr : users) {
		     if(usr.getID().equals(ID))
		    	 throw new Exception("Error: this ID already exist in the system");
		    }
	   if(!isValidEmailAddress(email))
		   throw new Exception("Error: invalid email address");
	   if(password.length()<8)
		   throw new Exception("Error: the password is too short");
	   newUser=new User(ID, password, name, email, 0, 0);
	   addUser(newUser);
   }
   
   public void addUser(User user){
	   
	   users.add(user);
	   
   }
   
   
   /**
    * in this function please see the Create game requirment in the Assignment 1 and add the relevant params according to the game preferences, 
    * @param user
    * @return true if the user can init game with the giver preferences, 
    */
   public boolean createGame(Player player){
	   
	   
	   
	   return false;
   }
   
   /**
    * see Search/filter activegames in assignment 1 and the relevant usecase
    * @param playerName
    * @param potSize
    * @return
    */
   public LinkedList<Game> Search(String playerName,int potSize){
	   
	   
	   return null;
   }
   
   public boolean joinGame(Game game, Player player){
	   
	   return false;
   }
   

@Override
public void editUserPassword(String userID, String newPassword)throws Exception {
	if(newPassword.isEmpty())
		throw new Exception("Error: empty password is invalid");
	if(newPassword.length()<8)
		throw new Exception("Error: the password is too short");
	
	for (User usr : users) {
	     if(usr.getID().equals(userID))
	     {
	    	usr.editPassword(newPassword);
	    	break;
	     }
	    }
}

@Override
public void editUserName(String userID, String newName)throws Exception {
	if(newName.isEmpty())
		throw new Exception("Error: empty name is invalid");
	
	for (User usr : users) {
	     if(usr.getID().equals(userID))
	     {
	    	usr.editName(newName);
	    	break;
	     }
	    }
	
}

@Override
public void editUserEmail(String userID, String newEmail)throws Exception {
	if(newEmail.isEmpty())
		throw new Exception("Error: empty email is invalid");
	if(!isValidEmailAddress(newEmail))
		   throw new Exception("Error: invalid email address");
	
	for (User usr : users) {
	     if(usr.getID().equals(userID))
	     {
	    	usr.editEmail(newEmail);
	    	break;
	     }
	    }
	
}  
   
}
