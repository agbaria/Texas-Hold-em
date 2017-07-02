package user;

import java.util.LinkedList;
import Game.GameInterface;
import communicationLayer.ConnectionHandler;

public interface UserInterface {

	public void getLog(LinkedList<String> i_game_logs);

	public void getLog(String i_game_logs);

	public void editName(String newName);

	public void editEmail(String newEmail);

	public void editPassword(String newPassword);

	public String getID();

	public void setID(String iD);

	public String getName();

	public void setName(String name);

	public String getEmail();

	public void setEmail(String email);

	public String getEncryptedPassword();

	public void setEncryptedPassword(String encPass);

	public int getTotalCash();

	public void setTotalCash(int totalCash);

	public int getScore();

	public void setScore(int score);

	public UserStatus getStatus();

	public void setStatus(UserStatus status);

	public int getLeague();

	public void setLeague(int league);

	public void GameUpdated(GameInterface game);

	public boolean takeAction(String GameID, int minBet);

	public void giveHandler(ConnectionHandler handler);

	boolean changeMoney(int money);

	public String getAvatar();

	public void setAvatar(String avatar);

	void actionMaked(String GameID);

	public void SendMSG(String msg);

	public void updateGamesPlayed();
	
	public void updateHighestWin(int win);
	
	public void updateAccumulatedWin(int win);
}
