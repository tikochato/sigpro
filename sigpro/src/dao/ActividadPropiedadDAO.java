package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;


import pojo.ActividadPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ActividadPropiedadDAO {

	public static List<ActividadPropiedad> getActividadPropiedadesPorTipoActividadPagina(int pagina,int idTipoActividad){
		List<ActividadPropiedad> ret = new ArrayList<ActividadPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ActividadPropiedad> criteria = session.createQuery("select p from ActividadPropiedad p "
					+ "inner join p.atipoPropiedads ptp "
					+ "inner join ptp.actividadTipo pt  "
					+ "where pt.id =  " + idTipoActividad,ActividadPropiedad.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ActividadPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalActividadPropiedades(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM ActividadPropiedad p where p.estado=1 ",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ActividadPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static List<ActividadPropiedad> getActividadPropiedadPaginaTotalDisponibles(int pagina, int numeroactividadpropiedades, String idPropiedades){
		List<ActividadPropiedad> ret = new ArrayList<ActividadPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ActividadPropiedad> criteria = session.createQuery("select p from ActividadPropiedad p  where p.estado = 1 "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " and p.id not in ("+ idPropiedades + ")" : "")
					,ActividadPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroactividadpropiedades)));
			criteria.setMaxResults(numeroactividadpropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("3", ActividadPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static ActividadPropiedad getActividadPropiedadPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ActividadPropiedad ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ActividadPropiedad> criteria = builder.createQuery(ActividadPropiedad.class);
			Root<ActividadPropiedad> root = criteria.from(ActividadPropiedad.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id )));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("4", ActividadPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarActividadPropiedad(ActividadPropiedad actividadPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(actividadPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", ActividadPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarActividadPropiedad(ActividadPropiedad actividadPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			actividadPropiedad.setEstado(0);
			session.update(actividadPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", ActividadPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarTotalActividadPropiedad(ActividadPropiedad actividadPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(actividadPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("7", ActividadPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<ActividadPropiedad> getActividadPropiedadesPagina(int pagina, int numeroActividadPropiedades,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion){
		List<ActividadPropiedad> ret = new ArrayList<ActividadPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT c FROM ActividadPropiedad c WHERE c.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<ActividadPropiedad> criteria = session.createQuery(query,ActividadPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroActividadPropiedades)));
			criteria.setMaxResults(numeroActividadPropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", ActividadPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalActividadPropiedad(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(c.id) FROM ActividadPropiedad c where c.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("9", ActividadPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ActividadPropiedad> getActividadPropiedadesPorTipoActividad(int idTipoActividad){
		List<ActividadPropiedad> ret = new ArrayList<ActividadPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ActividadPropiedad> criteria = session.createNativeQuery(" select cp.* "
				+ "from actividad_tipo ct "
				+ "join ctipo_propiedad ctp ON ctp.actividad_tipoid = ct.id "
				+ "join actividad_propiedad cp ON cp.id = ctp.actividad_propiedadid "
				+ " where ct.id = :idTipoComp",ActividadPropiedad.class);
			
			criteria.setParameter("idTipoComp", idTipoActividad);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("10", ActividadPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
