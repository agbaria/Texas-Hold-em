package Game;

import user.UserInterface;

public class Player implements PlayerInterface{
   
	
	 private Card[] hand;
	 private int cash;
	 private UserInterface User;
	 public Player (int cash,UserInterface User){
		 
		 this.cash = cash;
		 this.User = User;
		 hand = new Card[2];

		 
	 }
	 public UserInterface getUser(){
		 return User;
	 }
	 /**
	  * This method will be used by the Game to give the player cards
	  * */ 
	 public void giveCards(Card card1, Card card2){
		 hand[0] = card1;
		 hand[1] = card2;
	 }
	 
	 /**
	  * This method will be used by the Game to tell the player that the game updated, The player object will notify the user for this to update him
	  */
	 public void GameUpdated(GameInterface game){
		 User.GameUpdated(game);
		 
	 }
	 
	 
	 
	 /**
	  * This method will be used by the Game to tell the player that now is his turn, and request him to play, The player object request from the User to play
	  * @return true if the action was performed , else return false
	  */
	 public boolean takeAction(){
		 return User.takeAction();
		 
	 }
	 

	 


	 
	 /**
	  * This method will be used by the Game to give the player money that he win, The player object notify the User about this
	  * @return true if the action was performed , else return false
	  */
	 public boolean giveMoney(int money){
		 cash += money;
		 return User.giveMoney(money);
	 }

	
	public int getCash() {
		return cash;
	}

	
	public Card[] getCards() {
		return hand;
	}

	
	public boolean takeMoney(int money) {
		if(money > cash) 
		return false;
		cash -=money;
		return true;
	}
	
}
