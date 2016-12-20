package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.MetaTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class MetaTipoDAO {
	
	public static List<MetaTipo> getMetaTipos(){
		List<MetaTipo> ret = new ArrayList<MetaTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<MetaTipo> criteria = builder.createQuery(MetaTipo.class);
			Root<MetaTipo> root = criteria.from(MetaTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static MetaTipo getMetaTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		MetaTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<MetaTipo> criteria = builder.createQuery(MetaTipo.class);
			Root<MetaTipo> root = criteria.from(MetaTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarMetaTipo(MetaTipo MetaTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(MetaTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarMetaTipo(MetaTipo MetaTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			MetaTipo.setEstado(0);
			session.beginTransaction();
			session.update(MetaTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalMetaTipo(MetaTipo MetaTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(MetaTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<MetaTipo> getMetaTiposPagina(int pagina, int numeroMetaTipos){
		List<MetaTipo> ret = new ArrayList<MetaTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<MetaTipo> criteria = session.createQuery("SELECT m FROM MetaTipo m WHERE estado = 1",MetaTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroMetaTipos)));
			criteria.setMaxResults(numeroMetaTipos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalMetaTipos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(m.id) FROM MetaTipo m WHERE m.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
