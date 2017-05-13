package Game;

import user.UserInterface;

public interface PlayerInterface {
	 /**
	  * This method will be used by the Game to give the player cards
	  * */ 
	 public void giveCards(Card card1, Card card2);
	 
	 /**
	  * This method will be used by the Game to tell the player that the game updated, The player object will notify the user for this to update him
	  */
	 public void GameUpdated(GameInterface game);
	 
	 
	 /**
	  * This method will be used by the Game to tell the player that now is his turn, and request him to play, The player object request from the User to play
	  * @return true if the action was performed , else return false
	  */
	 public boolean takeAction();
	 
	 /**
	  * This method will be used by the Game to give the player money that he win, The player object notify the User about this
	  * @return true if the action was performed , else return false
	  */
	 public boolean giveMoney(int money);
	 
	 /**
	  * This method will be used by the Game to take from the player money in Blind bet, The player object notify the User about this
	  * @return true if the action was performed , else return false
	  */
	 public boolean takeMoney(int money);
	 
	 public int getCash();
	 
	 public Card[] getCards();

	 public UserInterface getUser();
}
