package dao;

import java.math.BigDecimal;
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
	
	private static Integer ESTADO_ACTIVO = 1;
	private static Integer ESTADO_CONGELADO = 2;
	
	public static List<MetaValor> getValoresMeta(int metaid, int pagina, int totalValores){
		List<MetaValor> ret = new ArrayList<MetaValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<MetaValor> criteria = builder.createQuery(MetaValor.class);
			Root<MetaValor> root = criteria.from(MetaValor.class);
			criteria.select( root );
		
			criteria.where( builder.and(builder.equal( root.get("id").get("metaid"), metaid ),builder.or(builder.equal(root.get("estado"), ESTADO_ACTIVO), builder.equal(root.get("estado"), ESTADO_CONGELADO))));
			
			session.createQuery( criteria ).setFirstResult(((pagina-1)*(totalValores)));
			session.createQuery( criteria ).setMaxResults(totalValores);
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
	
	public static List<MetaValor> getValoresMeta(int metaid, int estado){
		List<MetaValor> ret = new ArrayList<MetaValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<MetaValor> criteria = builder.createQuery(MetaValor.class);
			Root<MetaValor> root = criteria.from(MetaValor.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id").get("metaid"), metaid ),builder.equal(root.get("estado"), estado)));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("9", MetaValorDAO.class, e);
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
			
			String query = "SELECT mv FROM MetaValor mv WHERE mv.id.metaid = :metaid AND (mv.estado = :estado OR mv.estado = :estadoCongelado) ORDER BY mv.id.fecha desc";
			Query<MetaValor> criteria = session.createQuery(query,MetaValor.class);
			criteria.setParameter("metaid", metaid);
			criteria.setParameter("estado", ESTADO_ACTIVO);
			criteria.setParameter("estadoCongelado", ESTADO_CONGELADO);
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
			criteria.where( builder.and(builder.equal( root.get("id"), metavalorid), builder.or(builder.equal(root.get("estado"), ESTADO_ACTIVO), builder.equal(root.get("estado"), ESTADO_CONGELADO))));
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
			String query = "SELECT mv FROM MetaValor mv WHERE mv.metaid=:metaid AND (mv.estado = :estado OR mv.estado = :estadoCongelado )";
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<MetaValor> criteria = session.createQuery(query,MetaValor.class);
			criteria.setParameter("metaid", metaid);
			criteria.setParameter("estado", ESTADO_ACTIVO);
			criteria.setParameter("estadoCongelado", ESTADO_CONGELADO);
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
			String query = "SELECT count(mv.metaid) FROM MetaValor mv WHERE mv.metaid = :metaid AND (mv.estado = :estado OR mv.estado = :estadoCongelado)";
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("metaid", metaid);
			conteo.setParameter("estado", ESTADO_ACTIVO);
			conteo.setParameter("estadoCongelado", ESTADO_CONGELADO);
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
	
	public static BigDecimal getMetaValorPorMetaTipoObjetoObjetoTipo(Integer metaTipo, 
			Integer objetoId, Integer objetoTipo){
		Session session = CHibernateSession.getSessionFactory().openSession();
		BigDecimal ret = null;
		try{			
			
			String query = String.join(" ", "select sum(mv.valorDecimal) from Meta m",
							"inner join m.metaValors mv",
							"where m.estado = 1",
							"and m.metaTipo.id = ?1",
							"and m.objetoId = ?2",
							"and m.objetoTipo = ?3",
							"and (mv.estado = ?4 OR mv.estado = ?5)");
			Query<?> criteria = session.createQuery(query);
			criteria.setParameter("1", metaTipo);
			criteria.setParameter("2", objetoId);
			criteria.setParameter("3", objetoTipo);
			criteria.setParameter("4", ESTADO_ACTIVO);
			criteria.setParameter("5", ESTADO_CONGELADO);
			criteria.setMaxResults(1);
			ret = (BigDecimal) criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("8", MetaValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
