package dao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
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
}
