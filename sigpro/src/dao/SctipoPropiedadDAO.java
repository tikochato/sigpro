package dao;

import org.hibernate.Session;

import pojo.SctipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class SctipoPropiedadDAO {
	
	public static boolean eliminarSctipoPropiedad(SctipoPropiedad sctipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			//ctipoPropiedad.setEstado(0);
			session.beginTransaction();
			session.update(sctipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", SctipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalSctipoPropiedad(SctipoPropiedad sctipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(sctipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", SctipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
