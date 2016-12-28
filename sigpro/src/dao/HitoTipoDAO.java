package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.HitoTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class HitoTipoDAO {
	
	public static List<HitoTipo> getHitoTipos(){
		List<HitoTipo> ret = new ArrayList<HitoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<HitoTipo> criteria = builder.createQuery(HitoTipo.class);
			Root<HitoTipo> root = criteria.from(HitoTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", HitoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static HitoTipo getHitoTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		HitoTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<HitoTipo> criteria = builder.createQuery(HitoTipo.class);
			Root<HitoTipo> root = criteria.from(HitoTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", HitoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarHitoTipo(HitoTipo hitotipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(hitotipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", HitoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarHitoTipo(HitoTipo hitotipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			hitotipo.setEstado(0);
			session.beginTransaction();
			session.update(hitotipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", HitoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalHitoTipo(HitoTipo hitotipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(hitotipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", HitoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<HitoTipo> getHitoTiposPagina(int pagina, int numerohitotipo){
		List<HitoTipo> ret = new ArrayList<HitoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<HitoTipo> criteria = session.createQuery("SELECT c FROM HitoTipo c WHERE estado = 1",HitoTipo.class);
			criteria.setFirstResult(((pagina-1)*(numerohitotipo)));
			criteria.setMaxResults(numerohitotipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", HitoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalHitoTipos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(c.id) FROM HitoTipo c WHERE c.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", HitoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
