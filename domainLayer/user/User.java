package user;

import java.util.HashMap;
import java.util.LinkedList;

import com.sun.istack.internal.logging.Logger;

import Game.Card;
import Game.Game;
import Game.GameInterface;
import Game.Player;
import communicationLayer.ConnectionHandler;

public class User implements UserInterface {
	
	private	String ID;
	private String name;
	private String email;
	private String password;
	private int totalCash;
	private int score;
	private UserStatus status;
	private int league;
	private String avatar;
	Logger my_log;
	private boolean isWaitingForAction;
	private HashMap<String, Boolean> isWaitingForActionMap;
	private ConnectionHandler handler;

	public User(String ID, String password, String name, String email, int totalCash, int score, int league,String avatar){
		this.ID = ID;
		this.password=password;
		this.name = name;
		this.email = email;
		this.totalCash = totalCash ;
		this.score = score;
		isWaitingForAction=false;
		status = UserStatus.DISCONNECTED;
		my_log = Logger.getLogger(User.class);
		isWaitingForActionMap = new HashMap<String,Boolean>();
		this.league = league;
		this.setAvatar(avatar);
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

	/**
	 * HNAD= *TYPE,NUMBER,TYPE,NUMBER*
	 * *AVATAR*=STRING
	 * CASH= *NUMBER*
	 * PLAYERS = "*PLAYER USER NAME*,*PLAYER NAME*,*CASH*,*HAND*,*AVATAR* "{0,n}
	 * 
	 * CARDS = "*CARD NUMBER* *CARD TYPE* "{0,n}
	 * 
	 * GAME FULL DETAILS= "GameID=*ID*&players=*PLAYERS*&activePlayers=*PLAYERS*&blindBit=*NUMBER*&CurrentPlayer=*PLAYER USER NAME*&
	 * table=*CARDS*&MaxPlayers=*NUMBER*&activePlayersNumber=*NUMBER*&cashOnTheTable=*NUMBER*&CurrentBet=*NUMBER*"
	 * this function sends GAMEUPDATED message to the client "GAMEUPDATE *GAME FULL DETAILS*"
	 */
	public void GameUpdated(GameInterface game) {
		if(handler !=null&&this.status == UserStatus.CONNECTED)
		this.handler.send("GAMEUPDATE "+GameToString((Game)game));
		
	}

	public void SendMSG(String game){
		this.handler.send(game);
		
	}
	private String GameToString(Game game){

		String result="GameID="+game.getGameID();
		result= result+"&players=";
		Player[] players= game.getPlayers();
		for(int i=0;i<players.length;i++){
			String hand = getCardsPlayer(players, i);
			
			result = result+players[i].getUser().getID()+","+ players[i].getUser().getName()+"," +players[i].getUser().getTotalCash()+","+ hand+","+players[i].getUser().getAvatar()+",";
		}
		result = result + "&activePlayers=";
		 players= game.getActivePlayers();
		for(int i=0;i<players.length;i++){
			String hand = getCardsPlayer(players, i);
			
			result = result+players[i].getUser().getID()+","+ players[i].getUser().getName()+"," +players[i].getUser().getTotalCash()+","+ hand+","+players[i].getUser().getAvatar()+",";
		}
		result = result + "&blindBit="+game.getBlindBit();
		if(game.getCurrentPlayer()!=null) result = result+"&CurrentPlayer="+game.getCurrentPlayer().getUser().getID();
		else result = result+"&CurrentPlayer=";
		//result = result + game.getCurrentPlayer()!=null ? "&CurrentPlayer="+game.getCurrentPlayer().getUser().getID(): "&CurrentPlayer=";
		result = result + "&table=";
		
		for(int i=0;i<game.getCardsOnTable();i++){
			
			result = result+game.getTable()[i].getNumber()+","+ game.getTable()[i].getType()+",";

		}
		result = result + "&MaxPlayers="+game.getpreferences().getMaxPlayersNum();
		result = result + "&cashOnTheTable="+game.getCashOnTheTable();
		result = result + "&CurrentBet="+game.getCurrentBet();
		return result;
	}

	private String getCardsPlayer(Player[] players, int i) {
		String hand =""; 
		Card[]	PlayerCards=	players[i].getCards();
		if(PlayerCards[0]!=null&&PlayerCards[1]!=null){
			hand +=PlayerCards[0].getType()+" "+PlayerCards[0].getNumber()+" ";
			hand+=PlayerCards[1].getType()+" "+PlayerCards[1].getNumber();
		}
		else{
			hand+="NULL NULL NULL NULL";
		}
		return hand;
	}
	/**
	 * this function sends TAKEACTION request to the client to make some action "TAKEACTION *GAME ID*"
	 */
	@Override
	public boolean takeAction(String GameID,int minBit) {
		if(this.handler!=null&&this.status == UserStatus.CONNECTED){
		if(this.isWaitingForActionMap.containsKey(GameID))return false;
		else{
			
			this.isWaitingForActionMap.put(GameID, true);
		}
		
		
		this.handler.send("TAKEACTION "+GameID+" "+minBit);
		while(this.isWaitingForActionMap.get(GameID));
		this.isWaitingForActionMap.remove(GameID);
		return true;
	}
	return false;
}
	@Override
	public boolean changeMoney(int money) {
		if(totalCash  + money >0){
		totalCash+=money;
		return true;}
		return false;
	}




	public void giveHandler(ConnectionHandler handler) {
	    this.handler = handler;
	}

	@Override
	public void actionMaked(String GameID) {
		if(this.isWaitingForActionMap.containsKey(GameID)){
		this.isWaitingForActionMap.replace(GameID, false);
		}
	}
	public boolean isWaiting(){
		
		return this.isWaitingForAction;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
