package dbLayer;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import user.User;

public class MySessionFactory {
	private static SessionFactory sessionFactory = null;

	public static synchronized SessionFactory getInstance() {
		if (sessionFactory == null)
			setup();
		return sessionFactory;
	}

	private MySessionFactory() {

	}

	private static void setup() {
		/*
		String path = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "hibernate.cfg.xml";
		Configuration  configuration = new Configuration().configure(path);
		
		try {
			//sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
		//	StandardServiceRegistryBuilder.destroy(registry);
			e.printStackTrace();
		}
		*/
		sessionFactory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(User.class)
				.buildSessionFactory();
	}
}
