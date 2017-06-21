package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.MetaValor;
import pojo.MetaValorId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class MetaValorDAO {
	
	public static List<MetaValor> getValoresMeta(int metaid){
		List<MetaValor> ret = new ArrayList<MetaValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<MetaValor> criteria = builder.createQuery(MetaValor.class);
			Root<MetaValor> root = criteria.from(MetaValor.class);
			criteria.select( root ).where(builder.equal(root.get("id").get("metaid"),metaid));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", MetaValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static MetaValor getMetaValorPorMetaid(int metaid){
		Session session = CHibernateSession.getSessionFactory().openSession();
		MetaValor ret = null;
		try{			
			
			String query = "SELECT mv FROM MetaValor mv WHERE mv.id.metaid = :metaid ORDER BY mv.id.fecha desc";
			Query<MetaValor> criteria = session.createQuery(query,MetaValor.class);
			criteria.setParameter("metaid", metaid);
			criteria.setMaxResults(1);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", MetaValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static MetaValor getMetaValorPorId(MetaValorId metavalorid){
		Session session = CHibernateSession.getSessionFactory().openSession();
		MetaValor ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<MetaValor> criteria = builder.createQuery(MetaValor.class);
			Root<MetaValor> root = criteria.from(MetaValor.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("metaid"), metavalorid.getMetaid() ), builder.equal( root.get("fecha"), metavalorid.getFecha() )));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("3", MetaValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarMetaValor(MetaValor MetaValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(MetaValor);
			session.getTransaction().commit();
			
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", MetaValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
		
	public static boolean eliminarMetaValor(MetaValor MetaValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(MetaValor);
			
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", MetaValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<MetaValor> getMetaValorPagina(int pagina, int numeroMetasValor, int metaid,
			String columna_ordenada, String orden_direccion){
		List<MetaValor> ret = new ArrayList<MetaValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT mv FROM MetaValor mv WHERE mv.metaid=:metaid ";
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<MetaValor> criteria = session.createQuery(query,MetaValor.class);
			criteria.setParameter("metaid", metaid);
			criteria.setFirstResult(((pagina-1)*(numeroMetasValor)));
			criteria.setMaxResults(numeroMetasValor);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", MetaValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalMetaValor(int metaid){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(mv.metaid) FROM MetaValor mv WHERE mv.metaid = :metaid ";
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("metaid", metaid);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", MetaValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
