package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Riesgo;
import utilities.CHibernateSession;
import utilities.CLogger;


public class RiesgoDAO {
	public static List<Riesgo> getRiesgos(){
		List<Riesgo> ret = new ArrayList<Riesgo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Riesgo> criteria = builder.createQuery(Riesgo.class);
			Root<Riesgo> root = criteria.from(Riesgo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Riesgo getRiesgoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Riesgo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Riesgo> criteria = builder.createQuery(Riesgo.class);
			Root<Riesgo> root = criteria.from(Riesgo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarRiesgo(Riesgo riesgo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(riesgo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarRiesgo(Riesgo riesgo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			riesgo.setEstado(0);
			session.beginTransaction();
			session.update(riesgo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalRiesgo(Riesgo riesgo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(riesgo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Riesgo> getRiesgosPagina(int pagina, int numeroRiesgos){
		List<Riesgo> ret = new ArrayList<Riesgo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Riesgo> criteria = session.createQuery("SELECT r FROM Riesgo r WHERE r.estado = 1",Riesgo.class);
			criteria.setFirstResult(((pagina-1)*(numeroRiesgos)));
			criteria.setMaxResults(numeroRiesgos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRiesgos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(r.id) FROM Riesgo r WHERE r.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
