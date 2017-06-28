package serviceLayer;

import communicationLayer.ConnectionHandler;

public interface serviceLayerInterface {
/**
 * 	
 * @param request is string That has this format: "REG *USER NAME* *PASSWORD* *NAME* *EMAIL* *AVATAR*"
 * @return "REG DONE" if the registration done, "REG FAILED" else
 */
   public	String register(String request);

   /**
    * 
    * @param request is string That has this format: "LOGIN *USER NAME* *PASSWORD*"
    * @return "LOGIN DONE *USER NAME* *NAME* *CASH* *SCORE* *LEAGUE* *AVATAR*" if succeed to login, "LOGIN FAILED" else
    */
   public String login(String request,ConnectionHandler handler);
	
/**
 * 
 * @param id is string that has this format: "LOGOUT *USER NAME*"
 * @return "LOGOUT DONE" if succeed to logout, "LOGOUT FAILED *MSG*" else
 */
public	String logout(String id);

/**
 * 
 * @param request is string that has this format: "EDITPASS *USER NAME* *OLDPASSWORD* *NEWPASSWORD*"
 * @return "EDITPASS DONE" if succeed to edit the user password, "EDITPASS FAILED *MSG*" else
 */
public	String editUserPassword(String request);

/**
 * 
 * @param request is string that has this format: "EDITUSERNAME *USER NAME* *NEWNAME*"
 * @return "EDITUSERNAME DONE" if succeed to edit the user password, "EDITUSERNAME FAILED" else
 */
public String editUserName(String request);

/**
 * 
 * @param request is string that has this format: "EDITUSEREMAIL *USER NAME* *NEWEMAIL*"
 * @return "EDITUSEREMAIL DONE" if succeed to edit the user password, "EDITUSEREMAIL FAILED" else
 */
public String editUserEmail(String request);

/**
 * 
 * @param request is string that has this format: "EDITUSERAVATAR *USER NAME* *NEW AVATAR*"
 * @return "EDITUSERAVATAR DONE" if succeed to edit the user password, "EDITUSERAVATAR FAILED" else
 */
public String editUserAvatar(String request);

/** 
 * PLAYERS = "*PLAYER USER NAME*,*PLAYER NAME*,*CASH*,*HAND*,*AVATAR* "{0,n}
 * 
 * CARDS = "*CARD NUMBER* *CARD TYPE* "{0,n}
 * 
 * GAME FULL DETAILS= "GameID=*ID*&players=*PLAYERS*&activePlayers=*PLAYERS*&blindBit=*NUMBER*&CurrentPlayer=*PLAYER USER NAME*&
 * table=*CARDS*&MaxPlayers=*NUMBER*&activePlayersNumber=*NUMBER*&cashOnTheTable=*NUMBER*&CurrentBet=*NUMBER*"
 * 
 * GAME PREF = "gameTypePolicy=*GAME TYPE POLICY*&potLimit=*POT LIMIT*&buyInPolicy=*BUY IN POLICY*&chipPolicy=*CHIP POLICY*&minBet=*MIN BET*&minPlayersNum=*MIN PLAY NUM*&maxPlayersNum=*MAX PLAYER NUMBER*&spectatable=*T/F*&leaguable=*T/F*&league=*NUNBER*"
 * 
 * @param request is string that has this format: "CREATEGAME *USER NAME* *GAME PREF*"
 * @return "CREATEGAME *USER NAME* DONE *GAME FULL DETAILS*", "CREATEGAME FAILED" else
 */
public	String createGame(String request);

/** 
 *  GAMES DETAILS = "*ONE GAME DETAILS*"{0,n} 
 *  ONE GAME DETAILS= "GAMEID=*GAME ID* ENDGAME "
 * @param request is string that has this format: "LISTJOINABLEGAMES *USER NAME*"
 * @return "LISTJOINABLEGAMES DONE *GAMES DETAILS*", "LISTJOINABLEGAMES FAILED"
 */
public	String listJoinableGames(String request);

/** 
 *  GAMES DETAILS = "*ONE GAME DETAILS*"{0,n} 
 *  ONE GAME DETAILS= "GAMEID=*GAME ID* ENDGAME"  
 *  GAME PREF = gameTypePolicy=*GAME TYPE POLICY*&potLimit=*POT LIMIT*&buyInPolicy=*BUY IN POLICY*&chipPolicy=*CHIP POLICY*&minBet=*MIN BET*&minPlayersNum=*MIN PLAY NUM*&maxPlayersNum=*MAX PLAYER NUMBER*&spectatable=*T/F*&leaguable=*T/F*&league=*NUNBER*
 * @param request is string that has this format: "SEARCHGAMESBYPREFS *GAME PREF*"
 * @return "SEARCHGAMESBYPREFS DONE *GAMES DETAILS*", "SEARCHGAMESBYPREFS FAILED" 
 */
public	String searchGamesByPrefs(String request);

/** 
 *  GAMES DETAILS = "*ONE GAME DETAILS*"{0,n} 
 *  ONE GAME DETAILS= "GAMEID=*GAME ID* ENDGAME"
 * @param request is string that has this format: "SEARCHGAMESBYPLAYERNAME *USER NAME*"
 * @return "SEARCHGAMESBYPLAYERNAME DONE *GAMES DETAILS*","SEARCHGAMESBYPLAYERNAME FAILED" 
 */
public	String searchGamesByPlayerName(String request);

/** 
 *  GAMES DETAILS = "*ONE GAME DETAILS*"{0,n} 
 *  ONE GAME DETAILS= "GAMEID=*GAME ID* ENDGAME"
 * @param request is string that has this format: "SEARCHGAMESBYPOTSIZE *POT SIZE*"
 * @return "SEARCHGAMESBYPOTSIZE DONE *GAMES DETAILS*", "SEARCHGAMESBYPOTSIZE FAILED"
 */
public	String searchGamesByPotSize(String request);

/**

 * PLAYERS = "*PLAYER USER NAME*,*Player Name*,"{0,n} 
 *  CARDS = "*CARD NUMBER*,*CARD TYPE*,"{0,n}
 *  GAME FULL DETAILS= "GameID=*ID*&players=*PLAYERS*&activePlayers=*PLAYERS*&blindBit=*NUMBER*&CurrentPlayer=*PLAYER USER NAME*&
 * table=*CARDS*&MaxPlayers=*NUMBER*&cashOnTheTable=*NUMBER*&CurrentBet=*NUMBER*"
 * @param request is string that has this format: "JOINGAME *GAME ID* *USER NAME*"
 * @return "JOINGAME *GAME ID* *USER NAME* DONE *GAME FULL DETAILS*", "JOINGAME *GAME ID* *USER NAME* FAILED *MSG*"
 */
public	String joinGame(String request);

/** 
 *  GAMES DETAILS = "*ONE GAME DETAILS*"{0,n} 
 *  ONE GAME DETAILS= "GAMEID=*GAME ID* ENDGAME"
 * @param request is string that has this format: "LISTSPECTATEABLEGAMES"
 * @return "LISTSPECTATEABLEGAMES DONE *GAMES DETAILS*", "LISTSPECTATEABLEGAMES FAILED" 
 */
public	String listSpectatableGames(String id);


/** 
 * PLAYERS = "*PLAYER USER NAME*,*PLAYER NAME*,*CASH*,*HAND*,*AVATAR*, "{0,n}
 * CARDS = "*CARD NUMBER* *CARD TYPE* "{0,n}
 * GAME FULL DETAILS= "GameID=*ID*&players=*PLAYERS*&activePlayers=*PLAYERS*&blindBit=*NUMBER*&CurrentPlayer=*PLAYER USER NAME*&
 * table=*CARDS*&MaxPlayers=*NUMBER*&activePlayersNumber=*NUMBER*&cashOnTheTable=*NUMBER*&CurrentBet=*NUMBER*"
 * @param request is string that has this format: "SPECTATEGAME *GAME ID* *USER NAME*"
 * @return "SPECTATEGAME *GAME ID* *USER NAME* DONE *GAME FULL DETAILS*", "SPECTATEGAME FAILED *GAME ID* *USER NAME* *MSG*"
 */
public	String spectateGame(String request);

/**
 * 
 * @param request is string that has this format: "LEAVEGAME *GAME ID* *USER NAME*"
 * @return "LEAVEGAME *GAME ID* *USER NAME* DONE" if succeed to leave, "LEAVEGAME *GAME ID* *USER NAME* FAILED *MSG*" else
 */
public	String leaveGame(String request);

/** 
 * ACTION TYPE = FOLD/BET/CHECK
 * MONEY = NUMBER/NULL
 * @param action is string that has this format: "ACTION *ACTION TYPE* *GAME ID* *USER NAME* *MONEY*"
 * @return "ACTION *ACTION TYPE* *GAME ID* *USER NAME* DONE" if succeed to make the action,"ACTION *ACTION TYPE* *GAME ID* *USER NAME* FAILED" else
 */
public	String Action(String action);

/** 
 * ACTION TYPE = FOLD/BET/CHECK
 * MONEY = NUMBER/NULL
 * @param action is string that has this format: "CHATMSG *GameID* *UserID* *MSG*"
 */
public void ChatMsg(String action);

/**
 * 
 * @param msg is string that has this format: "WHISPERMSG *GameID* *UserID* *reciverID* *MSG*"
 */
public void WhisperMsg(String msg);

/**
 * 
 * @param request
 * @return
 */
public String ReplayGame(String request);

/**
 * 
 * @param gameID
 */
public void updateGame(String gameID);
	
}
