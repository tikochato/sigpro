package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import pojo.ActividadTipo;
import pojo.AtipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ActividadTipoDAO {

	public static List<ActividadTipo> getActividadTipos(){
		List<ActividadTipo> ret = new ArrayList<ActividadTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ActividadTipo> criteria = builder.createQuery(ActividadTipo.class);
			Root<ActividadTipo> root = criteria.from(ActividadTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ActividadTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
		

	
	public static ActividadTipo getActividadTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ActividadTipo ret = null;
		List<ActividadTipo> listRet = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ActividadTipo> criteria = builder.createQuery(ActividadTipo.class);
			Root<ActividadTipo> root = criteria.from(ActividadTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			listRet = session.createQuery( criteria ).getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("2", ActividadTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarActividadTipo(ActividadTipo actividadtipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(actividadtipo);
			session.flush();
			if(actividadtipo.getAtipoPropiedads()!=null){
				for (AtipoPropiedad propiedad : actividadtipo.getAtipoPropiedads()){
					session.saveOrUpdate(propiedad);	
				}
			}
			session.flush();
			
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
	
	public static boolean eliminarActividadTipo(ActividadTipo actividadTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			actividadTipo.setEstado(0);
			session.beginTransaction();
			session.update(actividadTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6",ActividadTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalActividadTipo(ActividadTipo actividadTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(actividadTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", ActividadTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
