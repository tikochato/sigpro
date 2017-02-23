package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProgramaPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProgramaPropiedadDAO {
	public static List<ProgramaPropiedad> getProgramaPropiedadesPorTipoProgramaPagina(int pagina,int idTipoPrograma){
		List<ProgramaPropiedad> ret = new ArrayList<ProgramaPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProgramaPropiedad> criteria = session.createQuery("select p from ProgramaPropiedad p "
					+ "inner join p.progtipoPropiedads ptp "
					+ "inner join ptp.programaTipo pt "
					+ "where pt.id =  " + idTipoPrograma + " "
					+ "and p.estado = 1",ProgramaPropiedad.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("1", ProgramaPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalProgramaPropiedades(String filtro_nombre,String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM ProgramaPropiedad p WHERE p.estado=1 ";
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
			CLogger.write("2", ProgramaPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static List<ProgramaPropiedad> getProgramaPropiedadPaginaTotalDisponibles(int pagina, int numeroprogramapropiedades, String idPropiedades){
		List<ProgramaPropiedad> ret = new ArrayList<ProgramaPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProgramaPropiedad> criteria = session.createQuery("select p from ProgramaPropiedad p  WHERE p.estado = 1 "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " and p.id not in ("+ idPropiedades + ")" : "") 
					,ProgramaPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroprogramapropiedades)));
			criteria.setMaxResults(numeroprogramapropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", ProgramaPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ProgramaPropiedad> getProgramaPropiedadesPorTipoPrograma(int idTipoPrograma){
		List<ProgramaPropiedad> ret = new ArrayList<ProgramaPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProgramaPropiedad> criteria = session.createNativeQuery(" select pp.* "
				+ "from programa_tipo pt "
				+ "join progtipo_propiedad ptp ON ptp.programa_tipoid = pt.id "
				+ "join programa_propiedad pp ON pp.id = ptp.programa_propiedadid "
				+ " where pt.id = :idTipoProy",ProgramaPropiedad.class);
			
			criteria.setParameter("idTipoProy", idTipoPrograma);
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
	
	public static ProgramaPropiedad getProgramaPropiedadPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProgramaPropiedad ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProgramaPropiedad> criteria = builder.createQuery(ProgramaPropiedad.class);
			Root<ProgramaPropiedad> root = criteria.from(ProgramaPropiedad.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal( root.get("estado"), 1 )));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("6", ProgramaPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalProgramaPropiedadesDisponibles(String idPropiedades){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("select count(p.id) from ProgramaPropiedad p  WHERE p.estado = 1 "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " and p.id not in ("+ idPropiedades + ")" : "") 
					,Long.class);
					
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", ProgramaPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ProgramaPropiedad> getProgramaPropiedadesPagina(int pagina, int numeroProgramaPropiedades,
			String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<ProgramaPropiedad> ret = new ArrayList<ProgramaPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT p FROM ProgramaPropiedad p where p.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<ProgramaPropiedad> criteria = session.createQuery(query,ProgramaPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroProgramaPropiedades)));
			criteria.setMaxResults(numeroProgramaPropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", ProgramaPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarProgramaPropiedad(ProgramaPropiedad programaPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(programaPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("9", ProgramaPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarProgramaPropiedad(ProgramaPropiedad programaPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			programaPropiedad.setEstado(0);
			session.beginTransaction();
			session.update(programaPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("10", ProgramaPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalProgramaPropiedad(ProgramaPropiedad programaPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(programaPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("11", ProgramaPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
