package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ComponenteTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ComponenteTipoDAO {

	/*public static List<Cooperante> getCooperantes(){
		List<Cooperante> ret = new ArrayList<Cooperante>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Cooperante> criteria = builder.createQuery(Cooperante.class);
			Root<Cooperante> root = criteria.from(Cooperante.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
		
	
	
	public static Cooperante getCooperantePorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Cooperante ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Cooperante> criteria = builder.createQuery(Cooperante.class);
			Root<Cooperante> root = criteria.from(Cooperante.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarCooperante(Cooperante cooperante){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(cooperante);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarCooperante(Cooperante cooperante){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			cooperante.setEstado(0);
			session.beginTransaction();
			session.update(cooperante);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalCooperante(Cooperante cooperante){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(cooperante);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	*/
	
	public static List<ComponenteTipo> getComponenteTiposPagina(int pagina, int numerocomponentestipo){
		List<ComponenteTipo> ret = new ArrayList<ComponenteTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ComponenteTipo> criteria = session.createQuery("SELECT c FROM ComponenteTipo c WHERE estado = 1",ComponenteTipo.class);
			criteria.setFirstResult(((pagina-1)*(numerocomponentestipo)));
			criteria.setMaxResults(numerocomponentestipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", ComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalComponenteTipo(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(c.id) FROM ComponenteTipo c WHERE c.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", ComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
