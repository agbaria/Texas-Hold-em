package Game;

import user.UserInterface;

public interface GameInterface {
	 /**
	  * this method will be used by the Player to make fold action
	  * @return true if the player can do this action in the current time, else  (this is not his turn, or he is not playing)return false
	  */
	 public boolean fold(UserInterface player);
	 
	 /**
	  * this method will be used by the Player to make check action
	  * @return true if the player can do this action in the current time, else  (this is not his turn, or he is not playing, or there's another player batted)return false
	  */
	 public boolean check(UserInterface player);
	 /**
	  * this method will be used by the Player to make bet action
	  * @return true if the player can do this action in the current time, else  (this is not his turn, or he is not playing, or there's another player batted more the @money, or he hasn't this amount of money)return false
	  */
	 public boolean bet(UserInterface player, int money);
	 /**
	  * this method will be used by the Player to make leaveGame action
	  * @return true if the player can do this action in the current time, else return false
	  */
	 public boolean leaveGame(UserInterface player);
	 
		/**
		 * This method Used by GameCenter to add new player to the game
		 * @param player this player will be holded by the relevant user 
		 * @return true if this player can join the Game, else (there's more than 8 players, or his cash not enough) return false
		 */
	 public boolean joinGame(UserInterface p);
		public boolean spectate(UserInterface p);
		public boolean isJoinAbleGame(UserInterface p);
		/**
		 * 
		 * @return the number of players in game
		 */
		public int getPlayerNumber();
		
		/**
		 * 
		 */
		public void GameUpated();
		
		/**
		 * 
		 */
		public void initializeGame();
}
