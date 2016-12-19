package dao;

import org.hibernate.Session;

import pojo.PtipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PtipoPropiedadDAO {
	public static boolean eliminarPtipoPropiedad(PtipoPropiedad ptipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			ptipoPropiedad.setEstado(0);
			session.beginTransaction();
			session.update(ptipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", PtipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalPtipoPropiedad(PtipoPropiedad ptipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(ptipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("2", PtipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
