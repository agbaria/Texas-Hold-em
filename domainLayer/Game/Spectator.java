package Game;

import user.UserInterface;

public class Spectator {

	 private UserInterface User;
	 public Spectator (UserInterface User){
		 

		 this.User = User;

		 
	 }
	 public UserInterface getUser(){
		 return User;
	 }

	 
	 /**
	  * This method will be used by the Game to tell the player that the game updated, The player object will notify the user for this to update him
	  */
	 public void GameUpdated(GameInterface game){
		 User.GameUpdated(game);
		 
	 }
	 


	
	
	
	
}
