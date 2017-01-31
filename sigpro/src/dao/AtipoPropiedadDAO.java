package dao;

import org.hibernate.Session;

import pojo.AtipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class AtipoPropiedadDAO {
	
	public static boolean eliminarAtipoPropiedad(AtipoPropiedad atipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			//atipoPropiedad.setEstado(0);
			session.beginTransaction();
			session.update(atipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", AtipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalAtipoPropiedad(AtipoPropiedad atipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(atipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", AtipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
