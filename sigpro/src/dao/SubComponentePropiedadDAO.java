package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.SubcomponentePropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class SubComponentePropiedadDAO {

	public static List<SubcomponentePropiedad> getSubComponentePropiedadesPorTipoSubComponentePagina(int pagina,int idTipoSubComponente){
		List<SubcomponentePropiedad> ret = new ArrayList<SubcomponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			//TODO: ctipoPropiedads
			Query<SubcomponentePropiedad> criteria = session.createQuery("select p from SubcomponentePropiedad p "
					+ "inner join p.ctipoPropiedads ptp "
					+ "inner join ptp.componenteTipo pt  "
					+ "where p.estado=1 and pt.id =  " + idTipoSubComponente + " ",SubcomponentePropiedad.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", SubComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalSubComponentePropiedades(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM SubcomponentePropiedad p where p.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}catch (NoResultException e){
			
		}catch(Throwable e){
			CLogger.write("2", SubComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static List<SubcomponentePropiedad> getSubComponentePropiedadPaginaTotalDisponibles(int pagina, int numerosubcomponentepropiedades, String idPropiedades){
		List<SubcomponentePropiedad> ret = new ArrayList<SubcomponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<SubcomponentePropiedad> criteria = session.createQuery("select p from SubcomponentePropiedad p  "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " where p.estado=1 and p.id not in ("+ idPropiedades + ")" : "")
					,SubcomponentePropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numerosubcomponentepropiedades)));
			criteria.setMaxResults(numerosubcomponentepropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("3", SubComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static SubcomponentePropiedad getSubComponentePropiedadPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		SubcomponentePropiedad ret = null;
		List<SubcomponentePropiedad> listRet = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<SubcomponentePropiedad> criteria = builder.createQuery(SubcomponentePropiedad.class);
			Root<SubcomponentePropiedad> root = criteria.from(SubcomponentePropiedad.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id )));
			listRet = session.createQuery( criteria ).getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("4", SubComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarSubComponentePropiedad(SubcomponentePropiedad subcomponentePropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(subcomponentePropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", SubComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarSubComponentePropiedad(SubcomponentePropiedad subcomponentePropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			subcomponentePropiedad.setEstado(0);
			session.beginTransaction();
			session.update(subcomponentePropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", SubComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarTotalSubComponentePropiedad(SubcomponentePropiedad subcomponentePropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(subcomponentePropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("7", SubComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<SubcomponentePropiedad> getSubComponentePropiedadesPagina(int pagina, int numeroSubComponentePropiedades , 
			String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<SubcomponentePropiedad> ret = new ArrayList<SubcomponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT c FROM SubcomponentePropiedad c WHERE c.estado=1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<SubcomponentePropiedad> criteria = session.createQuery(query,SubcomponentePropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroSubComponentePropiedades)));
			criteria.setMaxResults(numeroSubComponentePropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", SubComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalSubComponentePropiedad(String filtro_nombre, String filtro_usuario_creo 
			,String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT count(c.id) FROM SubcomponentePropiedad c WHERE c.estado=1";
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
			CLogger.write("9", SubComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<SubcomponentePropiedad> getSubComponentePropiedadesPorTipoComponente(int idTipoSubComponente){
		List<SubcomponentePropiedad> ret = new ArrayList<SubcomponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			//TODO: ctipo_propiedad
			Query<SubcomponentePropiedad> criteria = session.createNativeQuery(" select cp.* "
				+ "from componente_tipo ct "
				+ "join ctipo_propiedad ctp ON ctp.componente_tipoid = ct.id "
				+ "join componente_propiedad cp ON cp.id = ctp.componente_propiedadid "
				+ " where ct.id = :idTipoComp and cp.estado=1",SubcomponentePropiedad.class);
			
			criteria.setParameter("idTipoComp", idTipoSubComponente);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("10", SubComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
