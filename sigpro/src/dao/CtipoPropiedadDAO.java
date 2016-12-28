package dao;

import org.hibernate.Session;

import pojo.CtipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class CtipoPropiedadDAO {
	
	public static boolean eliminarCtipoPropiedad(CtipoPropiedad ctipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			//ctipoPropiedad.setEstado(0);
			session.beginTransaction();
			session.update(ctipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", CtipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalCtipoPropiedad(CtipoPropiedad ctipoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(ctipoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", CtipoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
