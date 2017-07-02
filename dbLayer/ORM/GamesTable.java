package ORM;

import java.sql.Connection;
import java.sql.DriverManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import Game.Game;

public class GamesTable {

	private static SessionFactory factory;
	private static Session session;

	private static void init() {

		// create session factory
		factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Game.class)
				.buildSessionFactory();

		// create session
		session = factory.getCurrentSession();
		session.beginTransaction();
	}
/*
	public static void main(String[] args) {

		TestConnection();
		StoreGame(new Game("200", "gdhh"));
		StoreGame(new Game("20", "gdhgnghh"));
		StoreGame(new Game("2", "gdhgngjkhhh"));
		RemoveGame("2");
		System.out.println(GetAllGames().size());
		UpdateGameDescription("5", "yahoo");
	}
*/
	/*
	 * Store a user to the users table and return its object
	 */
	public static void StoreGame(Game GameToStore) {

		init();
		String OldGameId = GameToStore.getGameID();
		String NewGameId;
		try {

			System.out.println("Saving the Game...");
			NewGameId = (String) session.save(GameToStore);
			// commit transaction
			session.getTransaction().commit();

			System.out.println("New Game stored with id: " + OldGameId);
		} finally {

			factory.close();
		}
		UpdateGameID(NewGameId, OldGameId);
	}

	public static void UpdateGameID(String UserId, String FieldValue) {
		UpdateGame(UserId, "id", FieldValue);
	}

	public static void UpdateGame(String GameId, String FieldName, String FieldValue) {

		init();

		try {

			System.out.println("Updating Game...");

			String query = "UPDATE games SET " + FieldName + " = '" + FieldValue + "' WHERE id = '" + GameId + "'";
			try {

				session.createSQLQuery(query).executeUpdate();
				session.getTransaction().commit();
				session.close();
			} catch (HibernateException erro) {
				session.getTransaction().rollback();
				session.close();
			}

			System.out.println("Done!");
		} finally {
			factory.close();
		}
	}

	public static void TestConnection() {

		String jdbcUrl = "jdbc:mysql://localhost:3306/poker_db?useSSL=false";
		String user = "poker";
		String pass = "poker";

		try {
			System.out.println("Connecting to database: " + jdbcUrl);

			Connection myConn = DriverManager.getConnection(jdbcUrl, user, pass);

			System.out.println("Connection successful!!!");

		} catch (Exception exc) {

			System.out.println("Connection Faild!!!");
			exc.printStackTrace();
		}

	}

	/*
	 * Get all Games as a list
	 */
	public static java.util.List GetAllGames() {

		init();
		java.util.List AllGames;
		try {

			System.out.println("\nGetting All Games");

			AllGames = session.createCriteria(Game.class).list();

		} finally {
			factory.close();
		}
		return AllGames;

	}

	/*
	 * delete a game from the games table and return its object
	 */
	public static Game RemoveGame(String GameId) {

		Game TargetGame;
		init();
		try {

			// retrieve game based on the id: primary key
			System.out.println("\nGetting Game with id: " + GameId);

			TargetGame = session.get(Game.class, GameId);

			if (TargetGame != null) {

				// delete the student
				System.out.println("Deleting Game: " + TargetGame);

				session.delete(TargetGame);

				// commit the transaction
				session.getTransaction().commit();
				System.out.println("Done!");
			} else {

				System.out.println("Game not exist!");
			}

		} finally {
			factory.close();
		}
		return TargetGame;
	}

	/*
	 * get a game from the games table and return its object
	 */
	public static Game GetGame(String GameId) {

		Game TargetGame;
		init();
		try {

			// retrieve Game based on the id: primary key
			System.out.println("\nGetting Game with id: " + GameId);

			TargetGame = session.get(Game.class, GameId);

			if (TargetGame != null) {

				// commit the transaction
				session.getTransaction().commit();
				System.out.println("Done!");
			} else {

				System.out.println("Game not exist!");
			}
		} finally {
			factory.close();
		}
		return TargetGame;
	}

	public static void UpdateGameDescription(String UserId, String FieldValue) {
		UpdateGame(UserId, "description", FieldValue);
	}

}
