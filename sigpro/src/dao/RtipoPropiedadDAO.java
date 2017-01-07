package dao;

import org.hibernate.Session;

import pojo.RtipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;


public class RtipoPropiedadDAO {
	
	public static boolean eliminarRtipoPropiedad(RtipoPropiedad rtipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			rtipoPropiedad.setEstado(0);
			session.beginTransaction();
			session.update(rtipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", RtipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalRtipoPropiedad(RtipoPropiedad rtipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(rtipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", RtipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
