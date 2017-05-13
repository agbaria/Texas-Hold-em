package user;

import java.util.LinkedList;

import Game.Game;
import Game.GameInterface;
import Game.Player;
import Game.Enum.GameType;

public interface UserInterface {

	public void getLog(LinkedList<String> i_game_logs);
	
	public void getLog(String  i_game_logs);
	
	public void editName(String newName) ;
	
	public void editEmail(String newEmail) ;
	
	public void editPassword(String newPassword) ;
	
	public String getID() ;

	public void setID(String iD) ;

	public String getName() ;

	public void setName(String name) ;

	public String getEmail() ;

	public void setEmail(String email) ;

	public String getPassword() ;

	public void setPassword(String password) ;

	public int getTotalCash() ;

	public void setTotalCash(int totalCash) ;

	public int getScore() ;

	public void setScore(int score) ;

	public UserStatus getStatus() ;

	public void setStatus(UserStatus status) ;

	public int getLeague() ;

	public void setLeague(int league) ;


	public void GameUpdated(GameInterface game) ;


	public boolean takeAction() ;



	public boolean giveMoney(int money) ;


	public boolean addPlayer(Player player) ;

	public boolean removePlayer(Player player) ;
}
