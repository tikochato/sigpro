package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;


import pojo.ProyectoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProyectoPropiedadDAO {
	
	
	public static List<ProyectoPropiedad> getProyectoPropiedadesPorTipoProyectoPagina(int pagina,int idTipoProyecto){
		List<ProyectoPropiedad> ret = new ArrayList<ProyectoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoPropiedad> criteria = session.createQuery("select p from ProyectoPropiedad p "
					+ "inner join p.ptipoPropiedads ptp "
					+ "inner join ptp.proyectoTipo pt "
					+ "where pt.id =  " + idTipoProyecto + " "
					+ "and p.estado = 1",ProyectoPropiedad.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("1", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalProyectoPropiedades(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM ProyectoPropiedad p WHERE p.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static List<ProyectoPropiedad> getProyectoPropiedadPaginaTotalDisponibles(int pagina, int numeroproyectopropiedades, String idPropiedades){
		List<ProyectoPropiedad> ret = new ArrayList<ProyectoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoPropiedad> criteria = session.createQuery("select p from ProyectoPropiedad p  WHERE p.estado = 1 "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " and p.id not in ("+ idPropiedades + ")" : "") 
					,ProyectoPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroproyectopropiedades)));
			criteria.setMaxResults(numeroproyectopropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ProyectoPropiedad> getProyectoPropiedadesPorTipoProyecto(int idTipoProyecto){
		List<ProyectoPropiedad> ret = new ArrayList<ProyectoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoPropiedad> criteria = session.createNativeQuery(" select pp.* "
				+ "from proyecto_tipo pt "
				+ "join ptipo_propiedad ptp ON ptp.proyecto_tipoid = pt.id "
				+ "join proyecto_propiedad pp ON pp.id = ptp.proyecto_propiedadid "
				+ " where pt.id = :idTipoProy",ProyectoPropiedad.class);
			
			criteria.setParameter("idTipoProy", idTipoProyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", RiesgoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static ProyectoPropiedad getProyectoPropiedadPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProyectoPropiedad ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProyectoPropiedad> criteria = builder.createQuery(ProyectoPropiedad.class);
			Root<ProyectoPropiedad> root = criteria.from(ProyectoPropiedad.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal( root.get("estado"), 1 )));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("6", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
