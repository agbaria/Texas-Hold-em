package System;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Game.Game;
import Game.GameInterface;
import Game.GamePreferences;
import Game.Player;
import Game.Enum.GameType;
import user.User;
import user.UserInterface;
import user.UserStatus;

public class GameCenter implements GameCenterInterface{
   private  ConcurrentLinkedQueue<User> users; 
   private ConcurrentLinkedQueue<Game> games;

   private static GameCenterInterface singleton = new GameCenter( );
   private LinkedList<GameLogs> game_logs;
   private final static Logger LOGGER = Logger.getLogger(GameCenter.class.getName());
   private AtomicInteger gameIdGen;
   
   public static GameCenterInterface getInstance( ) {
	      return singleton;
	   }
   
   private GameCenter(){
	   users = new ConcurrentLinkedQueue<User>();
	   games= new ConcurrentLinkedQueue<Game>();
	   game_logs = new  LinkedList<GameLogs>();
	   gameIdGen = new AtomicInteger(1);
   }
   
   
   public User getUser(String ID)
   {
	   User user = null;
	   for (User usr : users) 
		     if(usr.getID().equals(ID))
		    	 user=usr;
	   return user;
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
   public boolean register(String ID, String password, String name, String email) 
   {
	   User newUser;

	   for (User usr : users) 
		     if(usr.getID().equals(ID))
		     {
		    	 LOGGER.info("Error: this ID already exist in the system");
		         return false;
		     }
	   if(!isValidEmailAddress(email))
		   {
		    LOGGER.info("Error: invalid email address");
		    return false;
		   }
	   if(password.length()<8)
		   {
		     LOGGER.info("Error: the password is too short");
		     return false;
		   }
	   newUser=new User(ID, password, name, email, 0, 0,-1);
	   addUser(newUser);
	   return true;
   }
   
   public void addUser(User user){
	   
	   users.add(user);
	   
   }
   

   

	

   
   /**
    * in this function please see the Create game requirement in the Assignment 1 and add the relevant params according to the game preferences, 
    * @param user
    * @return true if the user can init game with the giver preferences, 
    */
   public boolean createGame(String UserID, GameType type,int Limit, int buyIn, int chipPolicy, int minBet, 
		   int minPlayers, int maxPlayers, boolean spectatable,boolean leaguable){
	   
	   GamePreferences preferences;
	   UserInterface creator = getUser(UserID);
	   try {
		   preferences = new GamePreferences(type,Limit, buyIn, chipPolicy, minBet, minPlayers, maxPlayers, spectatable,leaguable,creator.getLeague());
	   }
	   catch(Exception e) {
		   LOGGER.info("Error: game pregerences don't match requirements");
		   return false;
	   }
	   
	   Game newGame = new Game(preferences, gameIdGen.getAndIncrement()+"");
	   games.add(newGame);
	   if(joinGame(newGame.getGameID(),UserID)){
		   Thread th = new Thread(newGame);
		   th.start();
		   return true;
	   }
	   else{
		   games.remove(newGame);
	   }
	   return false;
   }
   
   /**
    * see Search/filter active games in assignment 1 and the relevant use case
    * @param playerName
    * @param potSize
    * @return
    */
   public LinkedList<Game> searchGamesByPotSize(int potSize){
	   
	  LinkedList<Game> can_join = new LinkedList<Game>();

		  for(Game i_game : games){			 
			  
				 if(i_game.getpreferences().getGameTypePolicy()==GameType.POT_LIMIT && i_game.getpreferences().getLimit()==potSize){
					  can_join.add(i_game);
				 
			  }}

		  
	  return  can_join;
   }
   
   public LinkedList<Game> searchGamesByPrefs(GamePreferences userPrefs){
	   
	  LinkedList<Game> can_join = new LinkedList<Game>();

		  for(Game i_game : games){			 
			  
				 if(userPrefs.checkEquality(i_game.getpreferences())){
					  can_join.add(i_game);
				 
			  }}

		  
	  return  can_join;
   }
   
   
   public LinkedList<Game> searchGamesByPlayerName(String name){
	   
	  LinkedList<Game> can_join = new LinkedList<Game>();

		  for(Game i_game : games){			 
			  Player[] p = i_game.getPlayers();
			  for(Player pp : p){
				 if(pp.getUser().getName().equals(name)){
					  can_join.add(i_game);
					  break;
				 }
			  }}

		  
	  return  can_join;
   }
   
   public LinkedList<Game> listJoinableGames(String UserID){
	   UserInterface user = getUser(UserID);
	   LinkedList<Game> can_join = new LinkedList<Game>();
	   for(Game game : games){
		   if(game.isJoinAbleGame(user))
			   can_join.add(game);
		   
		   
	   }
	   
	   return can_join;
	   
   }
   
   public LinkedList<Game> listSpectatableGames(){
	   LinkedList<Game> can_join = new LinkedList<Game>();
	   for(Game game : games){
		   if(game.getpreferences().isSpectatable())
			   can_join.add(game);
		   
		   
	   }
	   
	   return can_join;
	   
   }
   
   public boolean spectateGame(String UserID,String GameID){
	   GameInterface game = getGameByID(GameID);
	   UserInterface user = getUser(UserID);
	   if(user !=null && game!=null)
	   return game.spectate(user);
       return false;
   }
   
   public boolean joinGame(String gameID, String UserID){
	   GameInterface game = getGameByID(gameID);
	   UserInterface user = getUser(UserID);
	   if(game!=null && user !=null){
		   
		   return false;
		   
	   }
	   
	   return false;
   }
   
   
   public GameInterface getGameByID(String gameID){
	   for(Game game : games){
		   if(game.getGameID().equals(gameID))
		     return game;
		   
	   }
	   return null;
	   
	   
   }

	public boolean editUserPassword(String userID, String newPassword) {
		if(newPassword.isEmpty()) {
			  LOGGER.warning("Error: empty password is invalid");
			  return false;
		}
		if(newPassword.length() < 8) {
			  LOGGER.info("Error: the password is too short");
			  return false;
		}
		for (User usr : users) {
		     if(usr.getID().equals(userID)) {
		    	usr.editPassword(newPassword);
		    	return true;
		    	
		     }
		}
		return false;
	}

	public boolean editUserName(String userID, String newName) {
		if(newName.isEmpty()) {
			  LOGGER.info("Error: empty name is invalid");
			  return false;
		}
		
		for (User usr : users) {
		     if(usr.getID().equals(userID)) {
		    	usr.editName(newName);
		    	return true;
		     }
		}
		return false;
	}

	public boolean editUserEmail(String userID, String newEmail) {
		if(newEmail.isEmpty()) {
			  LOGGER.info("Error: empty email is invalid");
			  return false;
		}
		if(!isValidEmailAddress(newEmail)) {    
			LOGGER.info("Error: invalid email address");
			 return false;
		}
		for (User usr : users) {
		     if(usr.getID().equals(userID)) {
		    	usr.editEmail(newEmail);
		    	return true;
		     }
		}
		return false;
	}

	public boolean login(String ID, String password) {
		for (User usr : users) {
		     if(usr.getID().equals(ID)) {
		    	 if(usr.getPassword().equals(password)) {
		    		 usr.setStatus(UserStatus.CONNECTED);
		    		 return true;
		    	 }
		    	 else {
		    		 LOGGER.info("Error: incorrect password");
		    		 return false;
		    	 }
		     }
		}
		LOGGER.info("Error: unrecognize id");
		return false;
	}
	
	public void logout(String ID) {
		User user = getUser(ID);
		if(user!=null){
		for(Game g: games){
			
			leaveGame(g.getGameID(),ID);
			
		}
	     user.setStatus(UserStatus.DISCONNECTED);
	 		
		
		}
		}
	
	
	
	public boolean leaveGame(String GameID,String UserID){
		
		   Game game = (Game)getGameByID(GameID);
		   User user = getUser(UserID);
           return game.leaveGame(user);

	}

	public boolean check(String userID, String gameID) {
		GameInterface game = getGameByID(gameID);
		User user = getUser(userID);
		if(game != null && user != null) {
			return game.check(user);
		}
		return false;
	}

	public boolean bet(String userID, String gameID, int money) {
		GameInterface game = getGameByID(gameID);
		User user = getUser(userID);
		if(game != null && user != null) {
			return game.bet(user, money);
		}
		return false;
	}

	public boolean fold(String userID, String gameID) {
		GameInterface game = getGameByID(gameID);
		User user = getUser(userID);
		if(game != null && user != null) {
			return game.fold(user);
		}
		return false;
	}

	public boolean raise(String userID, String gameID, int money) {
		return bet(userID, gameID, money);
	}

	public boolean call(String userID, String gameID, int money) {
		return bet(userID, gameID, money);
	}
	
	

}
