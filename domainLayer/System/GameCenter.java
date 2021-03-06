package System;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Game.Game;
import Game.GameInterface;
import Game.GamePreferences;
import Game.Player;
import Game.Spectator;
import Game.Enum.GameType;
import ORM.UsersTable;
import communicationLayer.ConnectionHandler;
import user.User;
import user.UserInterface;
import user.UserStatus;

public class GameCenter implements GameCenterInterface {
	private ConcurrentLinkedQueue<User> users;
	private ConcurrentLinkedQueue<Game> games;

	private final static Logger LOGGER = Logger.getLogger(GameCenter.class.getName());
	private AtomicInteger gameIdGen;
	private static GameCenterInterface singleton = new GameCenter();

	public static GameCenterInterface getInstance() {
		return singleton;
	}

	private GameCenter() {
		users = new ConcurrentLinkedQueue<User>();
		games = new ConcurrentLinkedQueue<Game>();
		gameIdGen = new AtomicInteger(1);
	}

	public User getUser(String ID) {
		User user = null;
		for (User usr : users)
			if (usr.getID().equals(ID))
				user = usr;
		return user;
	}

	/**
	 * 
	 * @param email
	 * @return true if email is valid email address
	 */
	private boolean isValidEmailAddress(String email) {
		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
		Matcher mat = pattern.matcher(email);
		return mat.matches();
	}

	/**
	 * this function get all the details of unregistered user, check them under
	 * the game policy, if there is'nt problem with one or more of the details
	 * make a new user and add him to the system
	 * 
	 * @return true if the user can register to the system, else return false;
	 */
	public boolean register(String ID, String password, String name, String email, String avatar) {
		User newUser;

		for (User usr : users)
			if (usr.getID().equals(ID)) {
				LOGGER.info("Error: this ID already exist in the system");
				return false;
			}
		if (!isValidEmailAddress(email)) {
			LOGGER.info("Error: invalid email address");
			return false;
		}
		if (password.length() < 8) {
			LOGGER.info("Error: the password is too short");
			return false;
		}

		String encPswd = sha1(password);
		if (encPswd != null) {
			newUser = new User(ID, encPswd, name, email, 5000, 0, -1, avatar);
			UsersTable.StoreUser(newUser);
			// addUser(newUser);
			return true;
		} else
			return false;
	}

	public void addUser(User user) {

		users.add(user);

	}

	/**
	 * in this function please see the Create game requirement in the Assignment
	 * 1 and add the relevant params according to the game preferences,
	 * 
	 * @param user
	 * @return GAME ID if the user can init game with the giver preferences, ""
	 *         else
	 */
	public String createGame(String UserID, GamePreferences preference) {

		GamePreferences preferences;
		try {
			preferences = preference;
		} catch (Exception e) {
			LOGGER.info("Error: game pregerences don't match requirements");
			return "";
		}

		Game newGame = new Game(preferences, gameIdGen.getAndIncrement() + "");
		games.add(newGame);
		if (joinGame(newGame.getGameID(), UserID)) {
			Thread th = new Thread(newGame);
			th.start();
			return newGame.getGameID();
		} else {
			games.remove(newGame);
		}
		return "";
	}

	/**
	 * see Search/filter active games in assignment 1 and the relevant use case
	 * 
	 * @param playerName
	 * @param potSize
	 * @return
	 */
	public LinkedList<Game> searchGamesByPotSize(int potSize) {

		LinkedList<Game> can_join = new LinkedList<Game>();

		for (Game i_game : games) {
			if (i_game.getpreferences().getGameTypePolicy() == GameType.POT_LIMIT
					&& i_game.getpreferences().getLimit() == potSize) {
				can_join.add(i_game);

			}
		}

		return can_join;
	}

	public LinkedList<Game> searchGamesByPrefs(GamePreferences userPrefs) {

		LinkedList<Game> can_join = new LinkedList<Game>();

		for (Game i_game : games) {
			if (userPrefs.checkEquality(i_game.getpreferences())) {
				can_join.add(i_game);

			}
		}

		return can_join;
	}

	public LinkedList<Game> searchGamesByPlayerName(String name) {

		LinkedList<Game> can_join = new LinkedList<Game>();

		for (Game i_game : games) {
			Player[] p = i_game.getPlayers();
			for (Player pp : p) {
				if (pp.getUser().getName().equals(name)) {
					can_join.add(i_game);
					break;
				}
			}
		}

		return can_join;
	}

	public LinkedList<Game> listJoinableGames(String UserID) {
		UserInterface user = getUser(UserID);
		LinkedList<Game> can_join = new LinkedList<Game>();
		for (Game game : games) {
			if (game.isJoinAbleGame(user))
				can_join.add(game);

		}

		return can_join;

	}

	public LinkedList<Game> listSpectatableGames() {
		LinkedList<Game> can_join = new LinkedList<Game>();
		for (Game game : games) {
			if (game.getpreferences().isSpectatable())
				can_join.add(game);

		}

		return can_join;

	}

	public boolean spectateGame(String UserID, String GameID) {
		GameInterface game = getGameByID(GameID);
		UserInterface user = getUser(UserID);
		if (user != null && game != null)
			return game.spectate(user);
		return false;
	}

	public boolean joinGame(String gameID, String UserID) {
		GameInterface game = getGameByID(gameID);
		UserInterface user = getUser(UserID);
		if (game != null && user != null) {
			if (game.getPlayerNumber() == 0) {
				game.initializeGame();
				Thread th = new Thread((Game) game);
				th.start();
			}
			return game.joinGame(user);

		}

		return false;
	}

	public GameInterface getGameByID(String gameID) {
		for (Game game : games) {
			if (game.getGameID().equals(gameID))
				return game;

		}
		return null;

	}

	public boolean editUserPassword(String userID, String oldPassword, String newPassword) {
		if (newPassword.isEmpty()) {
			LOGGER.warning("Error: empty password is invalid");
			return false;
		}
		if (newPassword.length() < 8) {
			LOGGER.info("Error: the password is too short");
			return false;
		}
		for (User usr : users) {
			if (usr.getID().equals(userID) && usr.getEncryptedPassword().equals(sha1(oldPassword))) {

				usr.editPassword(sha1(newPassword));
				return true;

			}
		}
		return false;
	}

	public boolean editUserName(String userID, String newName) {
		if (newName.isEmpty()) {
			LOGGER.info("Error: empty name is invalid");
			return false;
		}

		for (User usr : users) {
			if (usr.getID().equals(userID)) {
				usr.editName(newName);
				return true;
			}
		}
		return false;
	}

	public boolean editUserEmail(String userID, String newEmail) {
		if (newEmail.isEmpty()) {
			LOGGER.info("Error: empty email is invalid");
			return false;
		}
		if (!isValidEmailAddress(newEmail)) {
			LOGGER.info("Error: invalid email address");
			return false;
		}
		for (User usr : users) {
			if (usr.getID().equals(userID)) {
				usr.editEmail(newEmail);
				return true;
			}
		}
		return false;
	}

	public boolean editUserAvatar(String userID, String newAvatar) {
		if (newAvatar.isEmpty()) {
			LOGGER.info("Error: empty AVATAR is invalid");
			return false;
		}
		for (User usr : users) {
			if (usr.getID().equals(userID)) {
				usr.setAvatar(newAvatar);
				return true;
			}
		}
		return false;
	}

	public boolean login(String ID, String password, ConnectionHandler handler) {
		/*
		 * for (User usr : users) { if (usr.getID().equals(ID)) { if
		 * (usr.getEncryptedPassword().equals(sha1(password))) {
		 * usr.setStatus(UserStatus.CONNECTED); usr.giveHandler(handler); return
		 * true; } else { LOGGER.info("Error: incorrect password"); return
		 * false; } } } LOGGER.info("Error: unrecognize id"); return false;
		 */

		User user = UsersTable.GetUser(ID);
		if (user != null) {
			String encPass = sha1(password);
			if (user.getEncryptedPassword().equals(encPass)) {
				user.setStatus(UserStatus.CONNECTED);
				user.giveHandler(handler);
				addUser(user);
				return true;
			} else {
				LOGGER.info("Error: incorrect password");
				return false;
			}
		}
		LOGGER.info("Error: unrecognize id");
		return false;

	}

	public void logout(String ID) {
		/*
		 * User user = getUser(ID); if (user != null) { for (Game g : games) {
		 * 
		 * leaveGame(g.getGameID(), ID);
		 * 
		 * } user.setStatus(UserStatus.DISCONNECTED);
		 * 
		 * }
		 */

		User user = getUser(ID);
		if (user != null) {
			for (Game g : games) {

				leaveGame(g.getGameID(), ID);

			}
			user.setStatus(UserStatus.DISCONNECTED);
			UsersTable.UpdateUser(user);
			users.remove(user);
		}

	}

	public boolean leaveGame(String GameID, String UserID) {

		Game game = (Game) getGameByID(GameID);
		User user = getUser(UserID);
		user.actionMaked(GameID);
		return game.leaveGame(user);

	}

	public boolean check(String userID, String gameID) {
		GameInterface game = getGameByID(gameID);
		User user = getUser(userID);
		if (game != null && user != null) {
			return game.check(user);
		}
		return false;
	}

	public boolean bet(String userID, String gameID, int money) {
		GameInterface game = getGameByID(gameID);
		User user = getUser(userID);
		if (game != null && user != null) {
			return game.bet(user, money);
		}
		return false;
	}

	public boolean fold(String userID, String gameID) {
		GameInterface game = getGameByID(gameID);
		User user = getUser(userID);
		if (game != null && user != null) {
			return game.fold(user);
		}
		return false;
	}

	public boolean raise(String userID, String gameID, int money) {
		return bet(userID, gameID, money);
	}

	public boolean call(String userID, String gameID, int money) {
		return bet(userID, gameID, money);
	}

	/**
	 * FOR TESTING PURPOSE ONLY
	 */

	public void removeAll() {
		for (Game game : games) {
			game.killGame();
		}
		this.users = new ConcurrentLinkedQueue<User>();
		this.games = new ConcurrentLinkedQueue<Game>();

	}

	public void ChatMsg(String GameID, String UserID, String MsgParts) {

		Game currentGame = (Game) getGameByID(GameID);
		String userType = null;
		if (currentGame != null)
			for (Player p : currentGame.getPlayers()) {

				if (p.getUser().getID().equals(UserID)) {
					userType = "player";
					currentGame.SendMSG(MsgParts, userType);
					break;
				}

			}
		if (userType == null && currentGame != null)
			for (Spectator s : currentGame.getSpectators()) {

				if (s.getUser().getID().equals(UserID)) {
					userType = "spectator";
					currentGame.SendMSG(MsgParts, userType);
					break;
				}

			}
	}

	public void WhisperMsg(String GameID, String UserID, String receiverID, String MsgParts) {

		Game currentGame = (Game) getGameByID(GameID);
		boolean isPlayer = false;
		if (currentGame != null)
			for (Player p : currentGame.getPlayers()) {

				if (p.getUser().getID().equals(UserID)) {
					currentGame.WhisperMSG(MsgParts, receiverID);
					isPlayer = true;
					break;
				}

			}
		if (!isPlayer && currentGame != null)
			for (Spectator s : currentGame.getSpectators()) {

				if (s.getUser().getID().equals(UserID)) {
					currentGame.WhisperMSG(MsgParts, receiverID);
					break;
				}

			}
	}

	public String getGameReplay(String gameID) {
		for (Game g : this.games)
			if (g.getGameID().equals(gameID))
				return g.getGameReplay();
		return null;
	}

	private String sha1(String input) {
		MessageDigest mDigest;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.info("ERROR: couldn't encrypt password: " + input);
			e.printStackTrace();
			return null;
		}
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

	public String getLeaderboard(LeaderboardType type) {
		switch (type) {
		case TotalGrossProfit:
			return toJSON(getLeadersBy("TOTALCASH"));
		case HighestCashGain:
			return toJSON(getLeadersBy("HIGHESTWIN"));
		case GamesPlayedNumber:
			return toJSON(getLeadersBy("GAMESPLAYED"));
		default:
			return null;
		}
	}

	private String toJSON(ArrayList<String> leaders) {
		StringBuilder builder = new StringBuilder("");
		builder.append("{\"leaders\": [");
		for (int i = 0; i < leaders.size(); i++) {
			builder.append(leaders.get(i));
			if (i < leaders.size() - 1)
				builder.append(", ");
		}
		builder.append("]}");
		return builder.toString();
	}

	private ArrayList<String> getLeadersBy(String c) {
		List<User> usrs = UsersTable.getTopStatBy(c);
		ArrayList<String> s = new ArrayList<>();
		for (User u : usrs) {
			String name = u.getName();
			String email = u.getEmail();
			String avatar = u.getAvatar();
			int cash = u.getTotalCash();
			int hw = u.getHighestWin();
			int gp = u.getGamesPlayed();
			int league = u.getLeague();

			String str = "{\"name\": \"" + name + "\", \"email\": \"" + email + "\", \"avatar\": \"" + avatar
					+ "\", \"totalCash\": " + cash + ", \"highestWin\": " + hw + ", \"gamesPlayed\": " + gp
					+ ", \"league\": " + league + "}";
			s.add(str);
		}
		return s;
	}

	@Override
	public String getUserStats(int type, String username) {
		User user = UsersTable.GetUser(username);
		if (user == null)
			return "User doesn't exist";

		double stat;
		if (type == 1)
			stat = (user.getAccumulatedWin() * 1.0) / user.getGamesPlayed();
		else
			stat = (user.getTotalCash() * 1.0) / user.getGamesPlayed();

		String name = user.getName();

		return "{\"name\": \"" + name + "\", \"stat\": \"" + stat + "\"}";
	}
}
