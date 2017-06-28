package serviceLayer;

import java.util.LinkedList;
import Game.Card;
import Game.Game;
import Game.GameInterface;
import Game.GamePreferences;
import Game.Player;
import Game.Enum.GameType;
import System.GameCenter;
import System.GameCenterInterface;
import communicationLayer.ConnectionHandler;
import user.UserInterface;

public class serviceLayer implements serviceLayerInterface {
	   private static serviceLayerInterface singleton = new serviceLayer( );
	   public static serviceLayerInterface getInstance( ) {
		      return singleton;
		   }
	
	
    GameCenterInterface gameCenter;
	
	public serviceLayer(){
		
		gameCenter = GameCenter.getInstance();
		
	}
	@Override
	public String register(String request) {
         String[] requests = request.split(" ");
          if(requests[0].equals("REG") && requests.length == 6){
        	  
        	  if(gameCenter.register(requests[1], requests[2], requests[3], requests[4],requests[5])){
        		  
        		  return "REG DONE";
        		  
        	  }
        	  
        	  
          }  
		
		
		return "REG FAILED";
	}

	@Override
	public String login(String request,ConnectionHandler handler) {
        String[] requests = request.split(" ");
        if(requests[0].equals("LOGIN") && requests.length == 3){
      	  
      	  if(gameCenter.login(requests[1], requests[2],handler)){
      		  
      		  UserInterface user=gameCenter.getUser(requests[1]);
      		  return "LOGIN DONE "+user.getID()+" "+user.getName()+" "+user.getTotalCash()+" "+user.getScore()+" "+user.getLeague()+" "+user.getAvatar();
      		  
      	  }
      	  
      	  
        }  
		
		
		return "LOGIN FAILED";
	}

	@Override
	public String logout(String id) {
        String[] requests = id.split(" ");
        if(requests[0].equals("LOGOUT") && requests.length == 2){
      	  
      	  gameCenter.logout(requests[1]);
      		  
      		  return "LOGOUT DONE";
      		  
      	  
      	  
      	  
        }  
		
		
		return "LOGOUT FAILED NAME OR FULL INSTRUCTION MISSED";
	}

	@Override
	public String editUserPassword(String request) {
        String[] requests = request.split(" ");
        if(requests[0].equals("EDITPASS") && requests.length == 4){
      	  if(gameCenter.editUserPassword(requests[1], requests[2], requests[3])){
      		  
      		  return "EDITPASS DONE";
      		  
      	  }
      	  
      	  
        }  
		
		
		return "EDITPASS FAILED";
	}
	
	@Override
	public String editUserName(String request) {
        String[] requests = request.split(" ");
        if(requests[0].equals("EDITUSERNAME") && requests.length == 3){
      	  if(gameCenter.editUserName(requests[1], requests[2])){
      		  
      		  return "EDITUSERNAME DONE";
      		  
      	  }
      	  
      	  
        }  
		
		
		return "EDITUSERNAME FAILED";
	}
	
	@Override
	public String editUserEmail(String request) {
        String[] requests = request.split(" ");
        if(requests[0].equals("EDITUSEREMAIL") && requests.length == 3){
      	  if(gameCenter.editUserEmail(requests[1], requests[2])){
      		  
      		  return "EDITUSEREMAIL DONE";
      		  
      	  }
      	  
      	  
        }  
		
		
		return "EDITUSEREMAIL FAILED";
	}
	
	
	/**
	 * 
	 * @param request is string that has this format: "EDITUSERAVATAR *USER NAME* *NEW AVATAR*"
	 * @return "EDITUSERAVATAR DONE" if succeed to edit the user password, "EDITUSERAVATAR FAILED" else
	 */
	@Override
	public String editUserAvatar(String request) {
        String[] requests = request.split(" ");
        if(requests[0].equals("EDITUSERAVATAR") && requests.length == 3){
      	  if(gameCenter.editUserAvatar(requests[1], requests[2])){
      		  
      		  return "EDITUSERAVATAR DONE";
      		  
      	  }
      	  
      	  
        }  
		
		
		return "EDITUSERAVATAR FAILED";
	}

	@Override
	public String createGame(String request) {
        String[] requests = request.split(" ");
        
        if(requests[0].equals("CREATEGAME") && requests.length == 3){
        	
        	
        	GamePreferences gamePerf =parseGamePrefs(requests[2]);
        	String gameID =gameCenter.createGame(requests[1], gamePerf);
        	if(!gameID.equals("")){
        	return 	"CREATEGAME "+requests[1]+" DONE " +GameToString((Game)gameCenter.getGameByID(gameID));
        		
        	}else 
        		
      		  return "CREATEGAME FAILED";
      		  
      	  }
      	  
      	  
         
		
		
		return "CREATEGAME FAILED";
	}

	@Override
	public String listJoinableGames(String request) {
        String[] requests = request.split(" ");
        if(requests[0].equals("LISTJOINABLEGAMES") && requests.length == 2){
        LinkedList<Game> games=	gameCenter.listJoinableGames(requests[1]);
        String gameDetails = gamesToIDs(games);
      		  return "LISTJOINABLEGAMES DONE "+gameDetails;
      		  
      	  }
      	  
      	  
         
		
		
		return "LISTJOINABLEGAMES FAILED";
	}

	@Override
	public String searchGamesByPrefs(String request) {
        String[] requests = request.split(" ");
        
        if(requests[0].equals("SEARCHGAMESBYPREFS") && requests.length == 2){
        	
        	
        	LinkedList<Game> games=	gameCenter.searchGamesByPrefs(parseGamePrefs(requests[1]));

             String gameDetails = gamesToIDs(games);
      		  return "SEARCHGAMESBYPREFS DONE "+gameDetails;
      		  
      	  }
      	  
      	  
         
		
		
		return "SEARCHGAMESBYPREFS FAILED";
	}
	private String gamesToIDs(LinkedList<Game> games) {
		String gameDetails ="";
		 
		  
		 for (Game game: games){
			 
			 gameDetails=gameDetails +"GAMEID="+game.getGameID()+ " BUYIN="+game.getpreferences().getBuyInPolicy()+" SETS="+(game.getpreferences().getMaxPlayersNum()-game.getPlayerNumber()) +" ENDGAME "; 
			 
		 }
		return gameDetails;
	}

	
	private GamePreferences parseGamePrefs(String gamePrefs){
		 String [] prefs = gamePrefs.split("&");
		 GameType gameTypePolicy = GameType.NO_LIMIT;
		 int potLimit=-1,  buyInPolicy=0, chipPolicy=0,  minBet=0,  minPlayersNum=2,  maxPlayersNum=8,league=-1;
			boolean spectatable=true,leaguable=false;
			
	        //PREF = gameTypePolicy=*GAME TYPE POLICY*&potLimit=*POT LIMIT*&buyInPolicy=*BUY IN POLICY*&chipPolicy=*CHIP POLICY*&
	        //minBet=*MIN BET*&minPlayersNum=*MIN PLAY NUM*&maxPlayersNum=*MAX PLAYER NUMBER*&
	        //spectatable=*T/F*&leaguable=*T/F*&league=*NUNBER
			
			for(String pref:prefs){
				String [] prefParts =pref.split("=");
				if(prefParts[0].equals("gameTypePolicy")){
					if(prefParts[1].equals("NO_LIMIT")){
						gameTypePolicy = GameType.NO_LIMIT;
					}
					else if(prefParts[1].equals("LIMIT"))
						gameTypePolicy = GameType.LIMIT;
					else gameTypePolicy = GameType.POT_LIMIT;
				}
				else if(prefParts[0].equals("potLimit")){
					
					potLimit=Integer.parseInt(prefParts[1]);
					
				}
				else if(prefParts[0].equals("buyInPolicy")){
					buyInPolicy=Integer.parseInt(prefParts[1]);
					
				}
				else if(prefParts[0].equals("chipPolicy")){
					chipPolicy=Integer.parseInt(prefParts[1]);
					
				}
				else if(prefParts[0].equals("minBet")){
					minBet=Integer.parseInt(prefParts[1]);
					
				}
				else if(prefParts[0].equals("minPlayersNum")){
					minPlayersNum=Integer.parseInt(prefParts[1]);
					
				}
				else if(prefParts[0].equals("maxPlayersNum")){
					maxPlayersNum=Integer.parseInt(prefParts[1]);
					
				}
				else if(prefParts[0].equals("spectatable")){
					if(prefParts[1].equals("F"))
						spectatable = false;
					else 
						spectatable = true;
					
				}
				else if(prefParts[0].equals("leaguable")){
					if(prefParts[1].equals("F"))
						leaguable = false;
					else 
						leaguable = true;
					
					
				}
				else if(prefParts[0].equals("league")){
					league=Integer.parseInt(prefParts[1]);
				}
			}
			try {
				GamePreferences result = new GamePreferences(gameTypePolicy, potLimit, buyInPolicy, chipPolicy, minBet, minPlayersNum, maxPlayersNum, spectatable, leaguable, league);
				return result;
			} catch (Exception e) {
			return null;
			}
		 
		
	}
	@Override
	public String searchGamesByPlayerName(String request) {

        String[] requests = request.split(" ");
        if(requests[0].equals("SEARCHGAMESBYPLAYERNAME") && requests.length == 2){
        LinkedList<Game> games=	gameCenter.searchGamesByPlayerName(requests[1]);
        String gameDetails = gamesToIDs(games);
      		  return "SEARCHGAMESBYPLAYERNAME DONE "+gameDetails;
      		  
      	  }
      	  
      	  
         
		
		
		return "SEARCHGAMESBYPLAYERNAME FAILED";
	
	}

	@Override
	public String searchGamesByPotSize(String request) {
        String[] requests = request.split(" ");
        if(requests[0].equals("SEARCHGAMESBYPOTSIZE") && requests.length == 2){
        LinkedList<Game> games=	gameCenter.searchGamesByPotSize(Integer.parseInt(requests[1]));
        String gameDetails = gamesToIDs(games);
      		  return "SEARCHGAMESBYPOTSIZE DONE "+gameDetails;
      		  
      	  }
      	  
      	  
         
		
		
		return "SEARCHGAMESBYPOTSIZE FAILED";
	}

	@Override
	public String joinGame(String request) {
        String[] requests = request.split(" ");
        if(requests[0].equals("JOINGAME") && requests.length == 3){
      	  if(gameCenter.joinGame(requests[1], requests[2])){
              
      		
      		  
      		  return "JOINGAME "+requests[1]+" "+requests[2]+" DONE "+GameToString((Game)gameCenter.getGameByID(requests[1]));
      		  
      	  }
      	  return "JOINGAME FAILED Can't join the game";
      	  
      	  
        }  
		
		
		return "JOINGAME FAILED BAD INSTRUCTION";
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
		result = result + "&CurrentPlayer="+ (game.getCurrentPlayer() != null ? game.getCurrentPlayer().getUser().getID() : "");
		result = result + "&table=";
		
		for(int i=0;i<game.getCardsOnTable();i++){
			
			result = result+game.getTable()[i].getNumber()+","+ game.getTable()[i].getType()+",";

		}
		result = result + "&MaxPlayers="+game.getpreferences().getMaxPlayersNum();
		result = result + "&cashOnTheTable="+game.getCashOnTheTable();
		result = result + "&CurrentBet="+game.getCurrentBet();
		return result;
	}

	@Override
	public String listSpectatableGames(String request) {
        String[] requests = request.split(" ");
        if(requests[0].equals("LISTSPECTATEABLEGAMES") && requests.length == 1){
        LinkedList<Game> games=	gameCenter.listSpectatableGames();
        String gameDetails = gamesToIDs(games);
      		  return "LISTSPECTATEABLEGAMES DONE "+gameDetails;
      		  
      	  }
      	  
      	  
         
		
		
		return "LISTSPECTATEABLEGAMES FAILED";
	}

	@Override
	public String spectateGame(String request) {
        String[] requests = request.split(" ");
        if(requests[0].equals("SPECTATEGAME") && requests.length == 3){
      	  if(gameCenter.spectateGame(requests[2],requests[1])){
              
      		
      		  
      		  return "SPECTATEGAME "+requests[1]+" "+requests[2]+" DONE "+GameToString((Game)gameCenter.getGameByID(requests[1]));
      		  
      	  }
      	  return "SPECTATEGAME FAILED Can't spectate the game";
      	  
      	  
        }  
		
		
		return "SPECTATEGAME FAILED BAD INSTRUCTION";
	}

	@Override
	public String leaveGame(String request) {
        String[] requests = request.split(" ");
        if(requests[0].equals("LEAVEGAME") && requests.length == 3){
      	  if(gameCenter.leaveGame(requests[1], requests[2])){
      		  return "LEAVEGAME "+requests[1]+" "+requests[2]+" DONE";
      		  
      	  }
      	  
      	  
        }  
		
		
		return "LEAVEGAME "+requests[1]+" "+requests[2]+ " FAILED Can't Leave The Game Please Try Again Later";
	}

	@Override
	public String Action(String action) {
        String[] requests = action.split(" ");
        if(requests[0].equals("ACTION")){
        	if(requests[1].equals("FOLD") && requests.length==4){
        		if(gameCenter.fold(requests[3], requests[2])){
        			  return "ACTION " +requests[1]+" "+requests[2]+" "+requests[3]+" DONE";}
        	}
        	else if (requests[1].equals("CHECK") && requests.length==4){
        		if(gameCenter.check(requests[3], requests[2])){

      			  return "ACTION " +requests[1]+" "+requests[2]+" "+requests[3]+" DONE";}
        		
        	}
        	else if (requests[1].equals("BET") && requests.length==5){
        		if(gameCenter.bet(requests[3], requests[2],Integer.parseInt(requests[4])))
        			  return "ACTION " +requests[1]+" "+requests[2]+" "+requests[3]+" DONE";}
        		
        		
        	}
        	else 
        		  return "ACTION " +requests[1]+" "+requests[2]+" "+requests[3]+" FAILED";
          
		
		
        return "ACTION " +requests[1]+" "+requests[2]+" "+requests[3]+" FAILED";
	}
	/** 
	 * @param action is string that has this format: "CHATMSG *GameID* *UserID* *MSG*"
	 */
	public void ChatMsg(String action){
        String[] requests = action.split(" ");
        if(requests[0].equals("CHATMSG") && requests.length >= 3){
      	  gameCenter.ChatMsg(requests[1], requests[2],action);
 
        }  
	}
	
	
	/**
	 * 
	 * @param msg is string that has this format: "WHISPERMSG *GameID* *UserID* *reciverID* *MSG*"
	 */
	public void WhisperMsg(String msg)
	{
		String[] requests = msg.split(" ");
        if(requests[0].equals("WHISPERMSG") && requests.length >= 4){
      	  gameCenter.WhisperMsg(requests[1], requests[2], requests[3], msg);
        }
	}

	public String ReplayGame(String request)
	{
		String[] parts = request.split(" ");
		   if(parts[0].equals("REPLAY") && parts.length == 2){
			   String gameReplay=gameCenter.getGameReplay(parts[1]);
			   if(gameReplay!=null)
		        return "GAMEREPLAY "+ " "+parts[1]+ " "+gameReplay +" DONE";
			   else 
				 return "GAMEREPLAY FAILED Can't replay the game";
		        }
		   return "GAMEREPLAY FAILED Can't replay the game";
	}
	
	public void updateGame(String gameID)
	{
		GameInterface game=gameCenter.getGameByID(gameID);
		if(game!=null)
		game.GameUpated();
	}
	

}
