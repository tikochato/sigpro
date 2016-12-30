package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;


import pojo.RiesgoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class RiesgoPropiedadDAO {
	public static List<RiesgoPropiedad> getRiesgoPropiedadesPorTipoRiesgoPagina(int pagina,int idTipoRiesgo){
		List<RiesgoPropiedad> ret = new ArrayList<RiesgoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RiesgoPropiedad> criteria = session.createQuery("select p from RiesgoPropiedad p " 
					+ "inner join p.rtipoPropiedads rtp " 
					+ "inner join rtp.riesgoTipo rt  "
					+ "where rt.id =  " + idTipoRiesgo + " ",RiesgoPropiedad.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", RiesgoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRiesgoPropiedades(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM RiesgoPropiedad p ",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", RiesgoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static List<RiesgoPropiedad> getRiesgoPropiedadPaginaTotalDisponibles(int pagina, int numeroriesgopropiedades, String idPropiedades){
		List<RiesgoPropiedad> ret = new ArrayList<RiesgoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RiesgoPropiedad> criteria = session.createQuery("select p from RiesgoPropiedad p  "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " where p.id not in ("+ idPropiedades + ")" : "") 
					,RiesgoPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroriesgopropiedades)));
			criteria.setMaxResults(numeroriesgopropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("3", RiesgoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static RiesgoPropiedad getRiesgoPropiedadPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		RiesgoPropiedad ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<RiesgoPropiedad> criteria = builder.createQuery(RiesgoPropiedad.class);
			Root<RiesgoPropiedad> root = criteria.from(RiesgoPropiedad.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal( root.get("estado"), 1 )));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("4", RiesgoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarRiesgoPropiedad(RiesgoPropiedad riesgoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(riesgoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", RiesgoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarRiesgoPropiedad(RiesgoPropiedad riesgoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			riesgoPropiedad.setEstado(0);
			session.beginTransaction();
			session.update(riesgoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", RiesgoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalRiesgoPropiedad(RiesgoPropiedad riesgoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(riesgoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("7", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<RiesgoPropiedad> getRiesgoPropiedadesPagina(int pagina, int numeroRiesgoPropiedades){
		List<RiesgoPropiedad> ret = new ArrayList<RiesgoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RiesgoPropiedad> criteria = session.createQuery("SELECT r FROM RiesgoPropiedad r ",RiesgoPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroRiesgoPropiedades)));
			criteria.setMaxResults(numeroRiesgoPropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", RiesgoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRiesgoPropiedad(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(r.id) FROM RiesgoPropiedad r ",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("9", RiesgoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
