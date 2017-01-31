package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;


import pojo.RecursoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class RecursoPropiedadDAO {

	public static List<RecursoPropiedad> getRecursoPropiedadesPorTipoRecursoPagina(int pagina,int idTipoRecurso){
		List<RecursoPropiedad> ret = new ArrayList<RecursoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RecursoPropiedad> criteria = session.createQuery("select p from RecursoPropiedad p "
					+ "inner join p.rectipoPropiedads ptp "
					+ "inner join ptp.recursoTipo pt  "
					+ "where pt.id =  " + idTipoRecurso + " ",RecursoPropiedad.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", RecursoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalRecursoPropiedades(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM RecursoPropiedad p ";
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", RecursoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static List<RecursoPropiedad> getRecursoPropiedadPaginaTotalDisponibles(int pagina, int numerorecursopropiedades, String idPropiedades){
		List<RecursoPropiedad> ret = new ArrayList<RecursoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RecursoPropiedad> criteria = session.createQuery("select p from RecursoPropiedad p  "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " where p.id not in ("+ idPropiedades + ")" : "")
					,RecursoPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numerorecursopropiedades)));
			criteria.setMaxResults(numerorecursopropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("3", RecursoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static RecursoPropiedad getRecursoPropiedadPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		RecursoPropiedad ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<RecursoPropiedad> criteria = builder.createQuery(RecursoPropiedad.class);
			Root<RecursoPropiedad> root = criteria.from(RecursoPropiedad.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id )));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("4", RecursoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarRecursoPropiedad(RecursoPropiedad recursoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(recursoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", RecursoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarRecursoPropiedad(RecursoPropiedad recursoPropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			//componentePropiedad.setEstado(0);
			session.beginTransaction();
			session.update(recursoPropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", RecursoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<RecursoPropiedad> getRecursoPropiedadesPagina(int pagina, int numeroRecursoPropiedades,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion){
		List<RecursoPropiedad> ret = new ArrayList<RecursoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT c FROM RecursoPropiedad c ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<RecursoPropiedad> criteria = session.createQuery(query,RecursoPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroRecursoPropiedades)));
			criteria.setMaxResults(numeroRecursoPropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", RecursoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalRecursoPropiedad(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(c.id) FROM RecursoPropiedad c ";
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
			CLogger.write("9", RecursoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}



}
