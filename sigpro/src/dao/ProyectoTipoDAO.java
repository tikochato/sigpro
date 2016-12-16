package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProyectoTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProyectoTipoDAO {

	public static List<ProyectoTipo> getProyectoTipos(){
		List<ProyectoTipo> ret = new ArrayList<ProyectoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProyectoTipo> criteria = builder.createQuery(ProyectoTipo.class);
			Root<ProyectoTipo> root = criteria.from(ProyectoTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ProyectoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static ProyectoTipo getProyectoTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProyectoTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProyectoTipo> criteria = builder.createQuery(ProyectoTipo.class);
			Root<ProyectoTipo> root = criteria.from(ProyectoTipo.class);
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
	
	public static boolean guardarProyectoTipo(ProyectoTipo proyectotipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(proyectotipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ProyectoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ProyectoTipo> getProyectosTipoPagina(int pagina, int numeroproyectotipos){
		List<ProyectoTipo> ret = new ArrayList<ProyectoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoTipo> criteria = session.createQuery("SELECT p FROM ProyectoTipo p WHERE estado = 1",ProyectoTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroproyectotipos)));
			criteria.setMaxResults(numeroproyectotipos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", ProyectoTipo.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalProyectoTipos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM Proyectotipo p WHERE p.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("5", ProyectoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarProyectoTipo(ProyectoTipo proyectoTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			proyectoTipo.setEstado(0);
			session.beginTransaction();
			session.update(proyectoTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", ProyectoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
