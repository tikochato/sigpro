package dao;

import org.hibernate.Session;

import pojo.ProgtipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;


public class ProgtipoPropiedadDAO {

	public static boolean eliminarProgtipoPropiedad(ProgtipoPropiedad progtipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			progtipoPropiedad.setEstado(0);
			session.beginTransaction();
			session.update(progtipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", ProgtipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalProgtipoPropiedad(ProgtipoPropiedad progtipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(progtipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("2", ProgtipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
