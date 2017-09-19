package dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import org.hibernate.query.Query;

import pojo.HitoResultado;
import utilities.CHibernateSession;
import utilities.CLogger;

public class HitoResultadoDAO {
	
	public static HitoResultado getHitoResultadoActivoPorHito(int hitoId){
		HitoResultado ret = null;
		List<HitoResultado> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<HitoResultado> criteria = session.createQuery("SELECT r FROM HitoResultado r WHERE r.hito.id = :hitoId and r.estado=1",HitoResultado.class);
			criteria.setParameter("hitoId", hitoId);
			
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("1", HitoResultadoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static HitoResultado getHitoResultadoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		HitoResultado ret = null;
		List<HitoResultado> listRet = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<HitoResultado> criteria = builder.createQuery(HitoResultado.class);
			Root<HitoResultado> root = criteria.from(HitoResultado.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			listRet = session.createQuery( criteria ).getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("2", HitoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarHitoResultado(HitoResultado hitoResultado){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(hitoResultado);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", HitoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarHitoResultado(HitoResultado hitoresultado){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			hitoresultado.setEstado(0);
			session.beginTransaction();
			session.update(hitoresultado);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", HitoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalHitoResultado(HitoResultado hitoresultado){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(hitoresultado);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", HitoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
