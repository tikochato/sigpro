package dao;

import org.hibernate.Session;

import pojo.RectipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class RectipoPropiedadDAO {
	
	public static boolean eliminarRectipoPropiedad(RectipoPropiedad rtipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			//ctipoPropiedad.setEstado(0);
			session.beginTransaction();
			session.update(rtipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", RectipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalRtipoPropiedad(RectipoPropiedad rtipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(rtipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", RectipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
