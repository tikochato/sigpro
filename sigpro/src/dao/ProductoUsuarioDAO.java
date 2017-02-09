package dao;

import org.hibernate.Session;

import pojo.ProductoUsuario;
import utilities.CHibernateSession;
import utilities.CLogger;


public class ProductoUsuarioDAO {
	
	public static boolean guardarProductoUsuario(ProductoUsuario productoUsuario){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(productoUsuario);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", ProductoUsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static boolean eliminarTotalProductoUsuario(ProductoUsuario productoUsuario){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(productoUsuario);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", ProductoUsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
