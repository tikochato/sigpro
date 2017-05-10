package utilities;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class CHibernateSessionSIGADE {
	
	private static  SessionFactory sessionFactory;
	
	static {
		
			
				Configuration configuration = new Configuration();

				try {
					InitialContext context = new InitialContext();
				    DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/sigade");
			    	Connection conn = ds.getConnection();
			    	conn.isClosed();
			    	configuration.configure();
			    	configuration.configure("hibernate_sigade.cfg.xml");
			    	conn.close();
			    }
			    catch(Exception e){
			    	e.printStackTrace();
			    }
			    sessionFactory = configuration.buildSessionFactory();
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static void changeEnvinroment(SessionFactory session){
		sessionFactory = session;
	}

}
