package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.MetaUnidadMedida;
import utilities.CHibernateSession;
import utilities.CLogger;

public class MetaUnidadMedidaDAO {
	
	public static List<MetaUnidadMedida> getMetaUnidadMedidas(){
		List<MetaUnidadMedida> ret = new ArrayList<MetaUnidadMedida>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<MetaUnidadMedida> criteria = builder.createQuery(MetaUnidadMedida.class);
			Root<MetaUnidadMedida> root = criteria.from(MetaUnidadMedida.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", MetaUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static MetaUnidadMedida getMetaUnidadMedidaPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		MetaUnidadMedida ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<MetaUnidadMedida> criteria = builder.createQuery(MetaUnidadMedida.class);
			Root<MetaUnidadMedida> root = criteria.from(MetaUnidadMedida.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", MetaUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarMetaUnidadMedida(MetaUnidadMedida MetaUnidadMedida){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(MetaUnidadMedida);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", MetaUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarMetaUnidadMedida(MetaUnidadMedida MetaUnidadMedida){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			MetaUnidadMedida.setEstado(0);
			session.beginTransaction();
			session.update(MetaUnidadMedida);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", MetaUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalMetaUnidadMedida(MetaUnidadMedida MetaUnidadMedida){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(MetaUnidadMedida);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", MetaUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<MetaUnidadMedida> getMetaUnidadMedidasPagina(int pagina, int numeroMetaUnidadMedidas){
		List<MetaUnidadMedida> ret = new ArrayList<MetaUnidadMedida>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<MetaUnidadMedida> criteria = session.createQuery("SELECT c FROM MetaUnidadMedida c WHERE estado = 1",MetaUnidadMedida.class);
			criteria.setFirstResult(((pagina-1)*(numeroMetaUnidadMedidas)));
			criteria.setMaxResults(numeroMetaUnidadMedidas);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", MetaUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalMetaUnidadMedidas(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(c.id) FROM MetaUnidadMedida c WHERE c.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", MetaUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
