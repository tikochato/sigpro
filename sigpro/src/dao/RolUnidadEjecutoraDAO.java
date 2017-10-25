package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.RolUnidadEjecutora;
import utilities.CHibernateSession;
import utilities.CLogger;

public class RolUnidadEjecutoraDAO {
	public static List<RolUnidadEjecutora> getRolesPorDefecto(){
		List<RolUnidadEjecutora> ret = new ArrayList<RolUnidadEjecutora>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RolUnidadEjecutora> criteria = session.createQuery("FROM RolUnidadEjecutora r where r.rolPredeterminado = 1 AND r.estado  = 1", RolUnidadEjecutora.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", RolUnidadEjecutoraDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	public static boolean guardarMiembro(RolUnidadEjecutora rolUnidadEjecutora){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(rolUnidadEjecutora);
			session.flush();
			session.flush();
			
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", RolUnidadEjecutoraDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalMiembro(RolUnidadEjecutora rolUnidadEjecutora){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(rolUnidadEjecutora);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ActividadTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<RolUnidadEjecutora> getRoles(){
		List<RolUnidadEjecutora> ret = new ArrayList<RolUnidadEjecutora>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RolUnidadEjecutora> criteria = session.createQuery("FROM RolUnidadEjecutora r where r.estado  = 1", RolUnidadEjecutora.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", RolUnidadEjecutoraDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
