package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Proyecto;
import pojo.ResponsableRol;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ResponsableRolDAO {
	public static List<ResponsableRol> getResponsableTiposPagina(int pagina, int numeroResponsableRol){
		List<ResponsableRol> ret = new ArrayList<ResponsableRol>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM ResponsableRol a ";
			Query<ResponsableRol> criteria = session.createQuery(query,ResponsableRol.class);
			
			criteria.setFirstResult(((pagina-1)*(numeroResponsableRol)));
			criteria.setMaxResults(numeroResponsableRol);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ResponsableRol.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalResponsableRol(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(t.id) FROM ResponsableRol t ";
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("2", ResponsableRol.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalResponsablesRoles(String filtro_nombre, String filtro_descripcion, String filtro_usuario_creo,
			String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM ResponsableRol p WHERE p.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_descripcion!=null && filtro_descripcion.trim().length()>0)
				query_a = String.join("", query_a, "p.descripcion LIKE '%", filtro_descripcion, "%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> criteria = session.createQuery(query,Long.class);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("4", ResponsableRol.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ResponsableRol> getResponsableRolPagina(int pagina, int numeroresponsabletipo,
			String filtro_nombre, String filtro_descripcion,String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<ResponsableRol> ret = new ArrayList<ResponsableRol>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT p FROM ResponsableRol p WHERE p.estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_descripcion!=null && filtro_descripcion.trim().length()>0)
				query_a = String.join("", query_a, " p.descripcion LIKE '%", filtro_descripcion,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<ResponsableRol> criteria = session.createQuery(query,ResponsableRol.class);
			criteria.setFirstResult(((pagina-1)*(numeroresponsabletipo)));
			criteria.setMaxResults(numeroresponsabletipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static ResponsableRol ResponsableRol(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ResponsableRol ret = null;
		try{
			Query<ResponsableRol> criteria = session.createQuery("FROM ResponsableRol where id=:id)", ResponsableRol.class);
			criteria.setParameter("id", id);
			 ret = criteria.getSingleResult();;
		}
		catch(Throwable e){
			CLogger.write("1", ResponsableRolDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarResponsableRol(ResponsableRol responsableRol){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(responsableRol);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", ResponsableRolDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarResponsableRol(ResponsableRol responsablerol){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			responsablerol.setEstado(0);
			session.beginTransaction();
			session.update(responsablerol);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("7", ResponsableTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
