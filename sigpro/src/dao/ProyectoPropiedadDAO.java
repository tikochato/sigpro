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
	
	public static Long getTotalProyectoPropiedades(String filtro_nombre,String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM ProyectoPropiedad p WHERE p.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> criteria = session.createQuery(query,Long.class);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static List<ProyectoPropiedad> getProyectoPropiedadPaginaTotalDisponibles(int pagina, int numeroproyectopropiedades, String idPropiedades,
			int numeroElementos){
		List<ProyectoPropiedad> ret = new ArrayList<ProyectoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoPropiedad> criteria = session.createQuery("select p from ProyectoPropiedad p  WHERE p.estado = 1 "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " and p.id not in ("+ idPropiedades + ")" : "") 
					,ProyectoPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroproyectopropiedades)));
			criteria.setMaxResults(numeroElementos);
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
			List<ProyectoPropiedad> lista = session.createQuery( criteria ).getResultList();
			ret = !lista.isEmpty() ? lista.get(0) : null;
			
		}
		catch(Throwable e){
			CLogger.write("6", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalProyectoPropiedadesDisponibles(String idPropiedades){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("select count(p.id) from ProyectoPropiedad p  WHERE p.estado = 1 "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " and p.id not in ("+ idPropiedades + ")" : "") 
					,Long.class);
					
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ProyectoPropiedad> getProyectoPropiedadesPagina(int pagina, int numeroProyectoPropiedades,
			String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<ProyectoPropiedad> ret = new ArrayList<ProyectoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT p FROM ProyectoPropiedad p where p.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<ProyectoPropiedad> criteria = session.createQuery(query,ProyectoPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroProyectoPropiedades)));
			criteria.setMaxResults(numeroProyectoPropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarProyectoPropiedad(ProyectoPropiedad proyectoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(proyectoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("9", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarProyectoPropiedad(ProyectoPropiedad proyectoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			proyectoPropiedad.setEstado(0);
			session.beginTransaction();
			session.update(proyectoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("10", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalProyectoPropiedad(ProyectoPropiedad proyectoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(proyectoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("11", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
