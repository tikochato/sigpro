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
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM RiesgoPropiedad p where p.estado = 1",Long.class);
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
			List<RiesgoPropiedad> listRet = null;
			CriteriaQuery<RiesgoPropiedad> criteria = builder.createQuery(RiesgoPropiedad.class);
			Root<RiesgoPropiedad> root = criteria.from(RiesgoPropiedad.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal( root.get("estado"), 1 )));
			listRet =session.createQuery( criteria ).getResultList();
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
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
			e.printStackTrace();
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
			CLogger.write("7", RiesgoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<RiesgoPropiedad> getRiesgoPropiedadesPagina(int pagina, int numeroRiesgoPropiedades,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion){
		List<RiesgoPropiedad> ret = new ArrayList<RiesgoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT r FROM RiesgoPropiedad r where r.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " r.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " r.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(r.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<RiesgoPropiedad> criteria = session.createQuery(query,RiesgoPropiedad.class);
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
	
	public static Long getTotalRiesgoPropiedad(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(r.id) FROM RiesgoPropiedad r where r.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " r.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " r.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(r.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			
			Query<Long> conteo = session.createQuery(query,Long.class);
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
	
	public static List<RiesgoPropiedad> getRiesgoPropiedadesPorTipoRiesgo(int idTipoRiesgo){
		List<RiesgoPropiedad> ret = new ArrayList<RiesgoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RiesgoPropiedad> criteria = session.createNativeQuery(" select rp.* "
				+ "from riesgo_tipo rt "
				+ "join rtipo_propiedad rtp ON rtp.riesgo_tipoid = rt.id "
				+ "join riesgo_propiedad rp ON rp.id = rtp.riesgo_propiedadid "
				+ " where rt.id = :idTipoRies AND rp.estado = 1",RiesgoPropiedad.class);
			
			criteria.setParameter("idTipoRies", idTipoRiesgo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("10", RiesgoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
