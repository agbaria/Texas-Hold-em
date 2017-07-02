package ORM;

import java.sql.DriverManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import dbLayer.MySessionFactory;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import user.User;

public class UsersTable {

	private static SessionFactory sessionFactory = MySessionFactory.getInstance();

	/*
	 * Store a user to the users table and return its object
	 */
	public static void StoreUser(User UserToStore) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String OldUserId = UserToStore.getID();
			System.out.println("Saving the User...");
			session.save(UserToStore);
			tx.commit();
			System.out.println("New User stored with id: " + OldUserId);

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/*
	 * delete a user from the users table and return its object
	 */
	public static User RemoveUser(String UserId) {
		User myUser = null;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			System.out.println("\nGetting User with id: " + UserId);
			myUser = session.get(User.class, UserId);

			if (myUser != null) {
				System.out.println("Deleting student: " + myUser);
				session.delete(myUser);
				tx.commit();
				System.out.println("Done!");

			} else {
				System.out.println("User not exist!");
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return myUser;
	}

	/*
	 * get a user from the users table and return its object
	 */
	public static User GetUser(String UserId) {
		User myUser = null;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			System.out.println("\nGetting User with id: " + UserId);
			myUser = session.get(User.class, UserId);

			if (myUser != null) {
				tx.commit();
				System.out.println("Done!");
			} else {
				System.out.println("User not exist!");
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return myUser;
	}

	/*
	 * check user password
	 */
	public static boolean CompairePasswords(String UserId, String Password) {
		User myUser = null;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			System.out.println("\nGetting User with id: " + UserId);
			myUser = session.get(User.class, UserId);

			if (myUser != null) {
				tx.commit();
				System.out.println("Done!");
			} else {
				System.out.println("User not exist!");
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return myUser == null ? false : myUser.getEncryptedPassword().equals(Password);
	}

	/*
	 * Get all users as a list
	 */
	public static List<User> GetAllUsers() {
		List<User> AllUsers = null;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			System.out.println("\nGetting All Users");

			CriteriaQuery<User> cq = session.getCriteriaBuilder().createQuery(User.class);
			cq.from(User.class);
			AllUsers = session.createQuery(cq).getResultList();

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return AllUsers;
	}

	public static void UpdateUserEmail(String UserId, String FieldValue) {
		UpdateUser(UserId, "email", FieldValue);
	}

	public static void UpdateUserID(String UserId, String FieldValue) {
		UpdateUser(UserId, "id", FieldValue);
	}

	public static void UpdateUserName(String UserId, String FieldValue) {
		UpdateUser(UserId, "name", FieldValue);
	}

	public static void UpdateUserTotalCash(String UserId, String FieldValue) {
		UpdateUser(UserId, "totalCash", FieldValue);
	}

	public static void UpdateUserScore(String UserId, String FieldValue) {
		UpdateUser(UserId, "score", FieldValue);
	}

	public static void UpdateUserLeague(String UserId, String FieldValue) {
		UpdateUser(UserId, "league", FieldValue);
	}

	public static void UpdateUserAvatar(String UserId, String FieldValue) {
		UpdateUser(UserId, "avatar", FieldValue);
	}

	public static void UpdateUser(String UserId, String FieldName, String FieldValue) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			System.out.println("Updating User...");
			String query = "UPDATE users SET " + FieldName + " = '" + FieldValue + "' WHERE id = '" + UserId + "'";
			try {
				session.createNativeQuery(query).executeUpdate();
				tx.commit();
			} catch (HibernateException erro) {
				tx.rollback();
			}
			System.out.println("Done!");

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public static void UpdateUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public static List<User> getTopStatBy(String c) {
		List<User> users = null;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			String query = "SELECT * FROM users ORDER BY " + c + " desc LIMIT 20";
			NativeQuery<User> q = session.createNativeQuery(query, User.class);
			users = q.getResultList();

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return users;
	}

	public static void TestConnection() {
		String jdbcUrl = "jdbc:mysql://localhost:3306/poker_db?useSSL=false";
		String user = "poker";
		String pass = "poker";

		try {
			System.out.println("Connecting to database: " + jdbcUrl);

			DriverManager.getConnection(jdbcUrl, user, pass);

			System.out.println("Connection successful!!!");

		} catch (Exception exc) {

			System.out.println("Connection Faild!!!");
			exc.printStackTrace();
		}

	}
/*
	public static void main(String[] args) {

		//TestConnection();
		for (int i=0;i<50;i++){
		StoreUser(new User("hf"+Math.random()*10020, "passwor43d", "na543me", "em453ail", (int)(Math.random()*100), (int)(Math.random()*100),(int)(Math.random()*100), "avatar"));
		}
//		RemoveUser("2");
//		System.out.println(GetUser("13"));
//		System.out.println(GetAllUsers().size());
//		UpdateUserEmail("1", "aa@aa");
		
//		UpdateUserName("4", "akjf");
//		UpdateUserTotalCash("6", "80");
//		UpdateUserScore("7", "800");
//		UpdateUserLeague("8", "3");
//		UpdateUserAvatar("9", "www");
//		System.out.println(CompairePasswords("1", "Password"));
//		System.out.println(CompairePasswords("1", "password"));
	}
*/
}
