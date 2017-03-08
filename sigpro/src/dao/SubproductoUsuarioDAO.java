package dao;

import org.hibernate.Session;

import pojo.SubproductoUsuario;
import utilities.CHibernateSession;
import utilities.CLogger;


public class SubproductoUsuarioDAO {
	
	public static boolean guardarSubproductoUsuario(SubproductoUsuario subproductoUsuario){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(subproductoUsuario);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", SubproductoUsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static boolean eliminarTotalProductoUsuario(SubproductoUsuario subproductoUsuario){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(subproductoUsuario);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", SubproductoUsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
