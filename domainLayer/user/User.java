package user;

import java.util.LinkedList;

import com.sun.istack.internal.logging.Logger;

import Game.Game;
import Game.GameInterface;
import Game.Player;
import Game.Enum.GameType;
import System.GameCenter;
import System.GameLogs;

public class User implements UserInterface {
	
	private	String ID;
	private String name;
	private String email;
	private String password;
	private LinkedList<Player> inGamePlayers;
	private int totalCash;
	private int score;
	private UserStatus status;
	private int league;
	Logger my_log;

	public User(String ID, String password, String name, String email, int totalCash, int score, int league){
		this.ID = ID;
		this.password=password;
		this.name = name;
		this.email = email;
		this.totalCash = totalCash;
		this.score = score;
		status = UserStatus.DISCONNECTED;
		my_log = Logger.getLogger(User.class);
		this.league = league;
	}
	
	public void getLog(LinkedList<String> i_game_logs){
		
		   for(String s :i_game_logs){
			   my_log.info(s);
		   }
	}
	
	public void getLog(String  i_game_logs){
		 
		  
		 my_log.info(i_game_logs);
		  
	}
	
	public void editName(String newName) {
		this.name = newName;
	}
	
	public void editEmail(String newEmail) {
		this.email = newEmail;
	}
	
	public void editPassword(String newPassword) {
		this.password = newPassword;
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(int totalCash) {
		this.totalCash = totalCash;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public int getLeague() {
		return league;
	}

	public void setLeague(int league) {
		this.league = league;
	}


	public void GameUpdated(GameInterface game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean takeAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean giveMoney(int money) {
		if(totalCash  + money >0){
		totalCash+=money;
		return true;}
		return false;
	}

	@Override
	public boolean addPlayer(Player player) {
		inGamePlayers.add(player);
		return true;
	}

	@Override
	public boolean removePlayer(Player player) {
		inGamePlayers.remove(player);
		return true;
	}

}
