package user;

import java.util.LinkedList;

import Game.Game;
import Game.Player;

public interface UserInterface {

	public void GameUpdated();

    public void editPassword(String newPassword);
	
	public void editName(String newName);
	
	public void editEmail(String newEmail);

	public boolean takeAction();


	public boolean fold();

	public String getName();
	public int geTotalCash();
	public boolean check();

	public boolean bet(int money);

	public boolean leaveGame();
	public void getLog(LinkedList<String> i_game_logs);
	public void getLog(String i_game_logs);


	public boolean giveMoney(int money);



	public boolean createGame(Player player);


	public LinkedList<Game> Search(String playerName,int potSize);

	public boolean joinGame(Game game, Player player);
}