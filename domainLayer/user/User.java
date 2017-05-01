package user;

import java.util.LinkedList;

import Game.Game;
import Game.Player;

public class User implements UserInterface {
	
private	String ID;
private String name;
private String email;
private String password;
private LinkedList<Player> inGamePlayers;
private int totalCash;
private int score;

public User(String ID, String password, String name, String email, int totalCash, int score){
	this.ID = ID;
	this.password=password;
	this.name = name;
	this.email = email;
	this.totalCash = totalCash;
	this.score = score;
	
}

public String toString()
{
	return "ID: "+ID+"\n"+"name: "+name+"\n";
}

public String getID()
{
	return ID;
}

public String getName()
{
	return name;
}

public String getEmail()
{
	return email;
}

public String getPassword()
{
	return password;
}


public void GameUpdated(){
	 
	 
}



public boolean takeAction(){
	 
	 
	 return false;
}


public boolean fold(){
	 
	 return false;
}


public boolean check(){
	 
	 return false;
}

public boolean bet(int money){
	 
	 return false;
}

public boolean leaveGame(){
	 
	 return false;
}


public boolean giveMoney(int money){
	 
	 return false;
}



public boolean createGame(Player player){
	   
	   
	   
	   return false;
}


public LinkedList<Game> Search(String playerName,int potSize){
	   
	   
	   return null;
}

public boolean joinGame(Game game, Player player){
	   
	   return false;
}

public void editPassword(String newPassword) {
	this.password=newPassword;
	
}

public void editName(String newName) {
	this.name=newName;
	
}

public void editEmail(String newEmail) {
	this.email=newEmail;
	
}

}
