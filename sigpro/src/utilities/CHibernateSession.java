package utilities;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CHibernateSession {

	private static  SessionFactory sessionFactory;
	private static  SessionFactory sessionFactory_analytic;
	private static boolean testState = TestSetUp.getTestState();
	
	static {
		if(!testState){
			try {
				Configuration configuration = new Configuration();
				Configuration configuration_analytic = new Configuration();

				try {
					InitialContext context = new InitialContext();
				    DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/sipro");
			    	Connection conn = ds.getConnection();
			    	conn.isClosed();
			    	configuration.configure();
			    	conn.close();
			    	
			    	InitialContext context_analytic = new InitialContext();
				    DataSource ds_analytic = (DataSource) context_analytic.lookup("java:comp/env/jdbc/sipro_analytic");
			    	Connection conn_analytic = ds_analytic.getConnection();
			    	conn_analytic.isClosed();
			    	configuration_analytic.configure();
			    	conn_analytic.close();
			    }
			    catch(Exception e){
			    	configuration.configure("hibernate_local.cfg.xml");
			    	configuration_analytic.configure("hibernate_sigade.cfg.xml");
			    }
			    
			    sessionFactory = configuration.buildSessionFactory();
			    sessionFactory_analytic = configuration_analytic.buildSessionFactory();
			    
			} catch (Throwable ex) {
				System.err.println("Initial SessionFactory creation failed." + ex);
				throw new ExceptionInInitializerError(ex);
			}
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static SessionFactory getSessionFactory_analytic() {
		return sessionFactory_analytic;
	}
	
	public static void changeEnvinroment(SessionFactory session){
		sessionFactory = session;
	}
}
