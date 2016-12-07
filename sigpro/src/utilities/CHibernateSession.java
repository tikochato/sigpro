package utilities;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CHibernateSession {

	private static final SessionFactory sessionFactory;

	static {
		try {
			Configuration configuration = new Configuration();

			try {
				InitialContext context = new InitialContext();
			    DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/sigpro");
		    	Connection conn = ds.getConnection();
		    	conn.isClosed();
		    	configuration.configure();
		    	conn.close();
		    }
		    catch(Exception e){
		    	configuration.configure("hibernate_local.cfg.xml");
		    }
		    
		    sessionFactory = configuration.buildSessionFactory();
		    
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
