package dao;

import org.hibernate.Session;

import pojo.FormularioItem;
import utilities.CHibernateSession;
import utilities.CLogger;

public class FormularioItemDAO {
	public static boolean eliminarFormularioItem(FormularioItem formularioItem){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			formularioItem.setEstado(0);
			session.beginTransaction();
			session.update(formularioItem);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", FormularioItem.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalFormularioItem(FormularioItem formularioItem){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(formularioItem);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("2", FormularioItem.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
