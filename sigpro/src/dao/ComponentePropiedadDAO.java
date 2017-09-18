package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;


import pojo.ComponentePropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ComponentePropiedadDAO {

	public static List<ComponentePropiedad> getComponentePropiedadesPorTipoComponentePagina(int pagina,int idTipoComponente){
		List<ComponentePropiedad> ret = new ArrayList<ComponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ComponentePropiedad> criteria = session.createQuery("select p from ComponentePropiedad p "
					+ "inner join p.ctipoPropiedads ptp "
					+ "inner join ptp.componenteTipo pt  "
					+ "where p.estado=1 and pt.id =  " + idTipoComponente + " ",ComponentePropiedad.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalComponentePropiedades(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM ComponentePropiedad p where p.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static List<ComponentePropiedad> getComponentePropiedadPaginaTotalDisponibles(int pagina, int numerocomponentepropiedades, String idPropiedades){
		List<ComponentePropiedad> ret = new ArrayList<ComponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ComponentePropiedad> criteria = session.createQuery("select p from ComponentePropiedad p  "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " where p.estado=1 and p.id not in ("+ idPropiedades + ")" : "")
					,ComponentePropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numerocomponentepropiedades)));
			criteria.setMaxResults(numerocomponentepropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("3", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static ComponentePropiedad getComponentePropiedadPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ComponentePropiedad ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ComponentePropiedad> criteria = builder.createQuery(ComponentePropiedad.class);
			Root<ComponentePropiedad> root = criteria.from(ComponentePropiedad.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id )));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("4", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarComponentePropiedad(ComponentePropiedad componentePropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(componentePropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarComponentePropiedad(ComponentePropiedad componentePropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			componentePropiedad.setEstado(0);
			session.beginTransaction();
			session.update(componentePropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarTotalComponentePropiedad(ComponentePropiedad componentePropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(componentePropiedad);
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

	public static List<ComponentePropiedad> getComponentePropiedadesPagina(int pagina, int numeroComponentePropiedades , 
			String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<ComponentePropiedad> ret = new ArrayList<ComponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT c FROM ComponentePropiedad c WHERE c.estado=1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<ComponentePropiedad> criteria = session.createQuery(query,ComponentePropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroComponentePropiedades)));
			criteria.setMaxResults(numeroComponentePropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalComponentePropiedad(String filtro_nombre, String filtro_usuario_creo 
			,String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT count(c.id) FROM ComponentePropiedad c WHERE c.estado=1";
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
			CLogger.write("9", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ComponentePropiedad> getComponentePropiedadesPorTipoComponente(int idTipoComponente){
		List<ComponentePropiedad> ret = new ArrayList<ComponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ComponentePropiedad> criteria = session.createNativeQuery(" select cp.* "
				+ "from componente_tipo ct "
				+ "join ctipo_propiedad ctp ON ctp.componente_tipoid = ct.id "
				+ "join componente_propiedad cp ON cp.id = ctp.componente_propiedadid "
				+ " where ct.id = :idTipoComp and cp.estado=1",ComponentePropiedad.class);
			
			criteria.setParameter("idTipoComp", idTipoComponente);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("10", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
