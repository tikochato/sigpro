package dao;
import org.hibernate.Session;

import pojo.Estadotabla;
import pojo.EstadotablaId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class EstadoTablaDAO {
	public static boolean saveEstadoTabla(Estadotabla estadotabla){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(estadotabla);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("3", EstadoTablaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static String getStadoTabla(String usuario, String tabla){
		Session session = CHibernateSession.getSessionFactory().openSession();
		String ret = "";
		try{
			EstadotablaId estadotablaid = new EstadotablaId(usuario,tabla);
			Estadotabla estadotabla = (Estadotabla) session.get(Estadotabla.class,estadotablaid);
			ret=estadotabla.getEstado();
		}catch(Throwable e){
			CLogger.write("3", EstadoTablaDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
		
	}
}
