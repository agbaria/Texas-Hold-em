package System;

import java.util.LinkedList;

import Game.Game;
import Game.GameInterface;
import Game.GamePreferences;
import communicationLayer.ConnectionHandler;
import user.User;

public interface GameCenterInterface {

	
	public User getUser(String ID);
	/**
	 * 
	 *this function get all the details of unregistered user, check them under the game policy, if there is'nt problem with one or more of the details make a new user and add him to the system
	 */
	public boolean register(String ID, String password, String name, String email,String avatar) ;
	   /**
		 * login to the system if the user registered
		 * @param ID
	     * @param password
	     * @return true if the user exist and logged in successfully, other way return false
		 */
	   public boolean login(String ID, String password,ConnectionHandler handler);
	   
	   /**
		 * logout from the system 
		 * @param ID
		 */
	   public void logout(String ID);
	
	   public void addUser(User user);
	   
	   /**
	    * in this function please see the Create game requirment in the Assignment 1 and add the relevant params according to the game preferences, 
	    * @param user
	    * @return true if the user can init game with the giver preferences, 
	    */
	   public String createGame(String UserID,GamePreferences preference);
	   
	   
	   public boolean joinGame(String gameID, String UserID);
	   
	   public boolean editUserPassword(String userID, String oldPassword, String newPassword);
	   public boolean editUserName(String userID, String newName);
	   public boolean editUserEmail(String userID, String newEmail);
	   public boolean editUserAvatar(String userID, String newAvatar);
	   
		 public LinkedList<Game> searchGamesByPotSize(int potSize);
		 public LinkedList<Game> searchGamesByPrefs(GamePreferences userPrefs);
		 public LinkedList<Game> searchGamesByPlayerName(String name);
		 public LinkedList<Game> listJoinableGames(String UserID);
		 public LinkedList<Game> listSpectatableGames();
		 public GameInterface getGameByID(String gameID);
		 public boolean leaveGame(String GameID,String UserID);
		 public boolean spectateGame(String UserID,String GameID);
		 public boolean check(String userID, String gameID);
		 public boolean bet(String userID, String gameID, int money);
		 public boolean fold(String userID, String gameID);
		 public boolean raise(String userID, String gameID, int money);
		 public boolean call(String userID, String gameID, int money);
		public void ChatMsg(String string, String string2, String copyOfRange);
		public void WhisperMsg(String gameID, String userID, String receiverID, String copyOfRange);
		public String getGameReplay(String gameID);
		 
		 
}
