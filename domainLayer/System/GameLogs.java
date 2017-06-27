package System;


import java.util.LinkedList;


public class GameLogs {
	private String IDGame;
	 LinkedList<String> log = new LinkedList<String>();
	 public GameLogs(String IDGame){
		 this.IDGame = IDGame;
		 
	 }
	 public void addLog(String l){
		 log.add(l);
		
	 }
	 public LinkedList<String> getLog(){
		 return log;
	 }
	 public String getGameID(){
		 return IDGame;
	 }
	 
	 public String getGameDescription(){
		 String description="";
		 for(String action: log)
			 description+=action+"&";
		 return description;
	 }
	 
	 
}
