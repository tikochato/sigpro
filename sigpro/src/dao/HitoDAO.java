package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Hito;
import utilities.CHibernateSession;
import utilities.CLogger;

public class HitoDAO {
	
	public static List<Hito> getHitos(){
		List<Hito> ret = new ArrayList<Hito>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Hito> criteria = builder.createQuery(Hito.class);
			Root<Hito> root = criteria.from(Hito.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", HitoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Hito getHitoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Hito ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Hito> criteria = builder.createQuery(Hito.class);
			Root<Hito> root = criteria.from(Hito.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", HitoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarHito(Hito hito){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(hito);
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
	
	public static boolean eliminarHito(Hito hito){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			hito.setEstado(0);
			session.beginTransaction();
			session.update(hito);
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
	
	public static boolean eliminarTotalHito(Hito hito){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(hito);
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
	
	public static List<Hito> getHitosPagina(int pagina, int numerohitos){
		List<Hito> ret = new ArrayList<Hito>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Hito> criteria = session.createQuery("SELECT h FROM Hito h WHERE h.estado = 1",Hito.class);
			criteria.setFirstResult(((pagina-1)*(numerohitos)));
			criteria.setMaxResults(numerohitos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", HitoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalHitos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(h.id) FROM Hito h WHERE h.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", HitoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Hito> getHitosPaginaPorProyecto(int pagina, int numerohitos, int proyectoid){
		List<Hito> ret = new ArrayList<Hito>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Hito> criteria = session.createQuery("SELECT h FROM Hito h WHERE h.estado = 1 and h.proyecto.id = :proyId",Hito.class);
			criteria.setParameter("proyId", proyectoid);
			criteria.setFirstResult(((pagina-1)*(numerohitos)));
			criteria.setMaxResults(numerohitos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", HitoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalHitosPorProyecto(int proyectoId){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(h.id) FROM Hito h WHERE h.estado=1 AND h.proyecto.id = :proyId",Long.class);
			conteo.setParameter("proyId", proyectoId);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", HitoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
