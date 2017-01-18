package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Proyecto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProyectoDAO implements java.io.Serializable  {

	
	private static final long serialVersionUID = 1L;

	public static List<Proyecto> getProyectos(){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Proyecto> criteria = builder.createQuery(Proyecto.class);
			Root<Proyecto> root = criteria.from(Proyecto.class);
			
			criteria.select( root );
			ret = (List<Proyecto>) session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean guardarProyecto(Proyecto proyecto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(proyecto);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Proyecto getProyectoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Proyecto ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Proyecto> criteria = builder.createQuery(Proyecto.class);
			Root<Proyecto> root = criteria.from(Proyecto.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("3", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarProyecto(Proyecto proyecto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			proyecto.setEstado(0);
			session.beginTransaction();
			session.update(proyecto);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalProyectos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM Proyecto p WHERE p.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("5", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Proyecto> getProyectosPagina(int pagina, int numeroproyecto){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Proyecto> criteria = session.createQuery("SELECT p FROM Proyecto p WHERE p.estado = 1",Proyecto.class);
			criteria.setFirstResult(((pagina-1)*(numeroproyecto)));
			criteria.setMaxResults(numeroproyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
}
