package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.RecursoUnidadMedida;
import utilities.CHibernateSession;
import utilities.CLogger;

public class RecursoUnidadMedidaDAO {
	
	public static List<RecursoUnidadMedida> getRecursoUnidadMedidas(){
		List<RecursoUnidadMedida> ret = new ArrayList<RecursoUnidadMedida>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<RecursoUnidadMedida> criteria = builder.createQuery(RecursoUnidadMedida.class);
			Root<RecursoUnidadMedida> root = criteria.from(RecursoUnidadMedida.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static RecursoUnidadMedida getRecursoUnidadMedidaPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		RecursoUnidadMedida ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<RecursoUnidadMedida> criteria = builder.createQuery(RecursoUnidadMedida.class);
			Root<RecursoUnidadMedida> root = criteria.from(RecursoUnidadMedida.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarRecursoUnidadMedida(RecursoUnidadMedida RecursoUnidadMedida){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(RecursoUnidadMedida);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarRecursoUnidadMedida(RecursoUnidadMedida RecursoUnidadMedida){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			RecursoUnidadMedida.setEstado(0);
			session.beginTransaction();
			session.update(RecursoUnidadMedida);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalRecursoUnidadMedida(RecursoUnidadMedida RecursoUnidadMedida){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(RecursoUnidadMedida);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<RecursoUnidadMedida> getRecursoUnidadMedidasPagina(int pagina, int numeroRecursoUnidadMedidas){
		List<RecursoUnidadMedida> ret = new ArrayList<RecursoUnidadMedida>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RecursoUnidadMedida> criteria = session.createQuery("SELECT c FROM RecursoUnidadMedida c WHERE estado = 1",RecursoUnidadMedida.class);
			criteria.setFirstResult(((pagina-1)*(numeroRecursoUnidadMedidas)));
			criteria.setMaxResults(numeroRecursoUnidadMedidas);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRecursoUnidadMedidas(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(c.id) FROM RecursoUnidadMedida c WHERE c.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
