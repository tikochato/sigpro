package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.RiesgoTipo;
import pojo.RtipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;


public class RiesgoTipoDAO {
	
	public static List<RiesgoTipo> getRiesgoTipos(){
		List<RiesgoTipo> ret = new ArrayList<RiesgoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<RiesgoTipo> criteria = builder.createQuery(RiesgoTipo.class);
			Root<RiesgoTipo> root = criteria.from(RiesgoTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", RiesgoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static RiesgoTipo getRiesgoTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		RiesgoTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<RiesgoTipo> criteria = builder.createQuery(RiesgoTipo.class);
			Root<RiesgoTipo> root = criteria.from(RiesgoTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", RiesgoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarRiesgoTipo(RiesgoTipo riesgotipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(riesgotipo);
			session.flush();
			
			if (riesgotipo.getRtipoPropiedads() !=null){
				for (RtipoPropiedad propiedad : riesgotipo.getRtipoPropiedads()){
					session.saveOrUpdate(propiedad);	
				}
			}
			session.flush();
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("3", RiesgoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	public static boolean eliminarRiesgoTipo(RiesgoTipo riesgoTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			riesgoTipo.setEstado(0);
			session.beginTransaction();
			session.update(riesgoTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4",RiesgoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	public static boolean eliminarTotalRiesgoTipo(RiesgoTipo riesgoTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(riesgoTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", RiesgoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static List<RiesgoTipo> getRiesgoTiposPagina(int pagina, int numeroriesgostipo){
		List<RiesgoTipo> ret = new ArrayList<RiesgoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RiesgoTipo> criteria = session.createQuery("SELECT r FROM RiesgoTipo r WHERE estado = 1",RiesgoTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroriesgostipo)));
			criteria.setMaxResults(numeroriesgostipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", RiesgoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRiesgoTipo(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(r.id) FROM RiesgoTipo r WHERE r.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", RiesgoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
