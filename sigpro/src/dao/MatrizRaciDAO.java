package dao;

import org.hibernate.Session;

import pojo.MatrizRaci;
import utilities.CHibernateSession;
import utilities.CLogger;

public class MatrizRaciDAO {
	
	public static boolean guardarMatrizRaci(MatrizRaci matrizRaci){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(matrizRaci);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", MatrizRaci.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
