package dao;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Permiso;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PermisoDAO {
	public static List<Permiso> getPermisos(){
		List <Permiso> ret = new ArrayList <Permiso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permiso> criteria = builder.createQuery(Permiso.class);
			Root<Permiso> root = criteria.from(Permiso.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", PermisoDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean guardarPermiso(Permiso permiso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(permiso);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("1",PermisoDAO.class,e);
		}finally{
			session.close();
		}
		
		return ret;
	}
	public static Permiso getPermiso(String nombrepermiso){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Permiso ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permiso> criteria = builder.createQuery(Permiso.class);
			Root<Permiso> root = criteria.from(Permiso.class);
			criteria.where( builder.and(builder.equal(root.get("nombre"), nombrepermiso)));
			ret = (Permiso) session.createQuery( criteria ).getSingleResult();
		}catch(Throwable e){
			CLogger.write("1",PermisoDAO.class,e);
		}finally{
			session.close();
		}
		 return ret;
	}
	public static boolean eliminarPermiso(Permiso permiso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			permiso.setEstado(0);
			session.beginTransaction();
			session.saveOrUpdate(permiso);
			session.getTransaction().commit();
			ret = true;
			
		}catch(Throwable e){
			CLogger.write("4",PermisoDAO.class,e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static Permiso getPermisoById(Integer idpermiso){
		Permiso ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			ret = (Permiso)session.get(Permiso.class,idpermiso );
		}catch(Throwable e){
			CLogger.write("1",PermisoDAO.class,e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Permiso> getPermisosPagina(int pagina, int numeroPermisos){
		List <Permiso> ret = new ArrayList <Permiso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query <Permiso> criteria = session.createQuery("FROM Permiso  where estado= :estado",Permiso.class);
			criteria.setParameter("estado", 1);
			criteria.setFirstResult(((pagina-1)*(numeroPermisos)));
			criteria.setMaxResults(numeroPermisos);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("6", HitoTipoDAO.class, e);		
		}finally{
			session.close();
		}
		return ret;
	}
	public static Long getTotalPermisos(){
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM Permiso p WHERE p.estado=1",Long.class);
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
