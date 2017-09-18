package dao;
import org.hibernate.Session;

import pojo.EstadoTabla;
import pojo.EstadoTablaId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class EstadoTablaDAO {
	public static boolean saveEstadoTabla(EstadoTabla estadotabla){
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
			EstadoTablaId estadotablaid = new EstadoTablaId(usuario,tabla);
			EstadoTabla estadotabla = session.get(EstadoTabla.class,estadotablaid);
			if(estadotabla!=null)
				ret=estadotabla.getValores();
		}catch(Throwable e){
			CLogger.write("3", EstadoTablaDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
		
	}
}
