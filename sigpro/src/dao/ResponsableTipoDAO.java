package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Proyecto;
import pojo.ResponsableTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ResponsableTipoDAO {
	
	public static ResponsableTipo ResponsableTipo(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ResponsableTipo ret = null;
		try{
			Query<ResponsableTipo> criteria = session.createQuery("FROM ResponsableTipo where id=:id)", ResponsableTipo.class);
			criteria.setParameter("id", id);
			 ret = criteria.getSingleResult();;
		}
		catch(Throwable e){
			CLogger.write("1", ResponsableTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ResponsableTipo> getResponsableTiposPagina(int pagina, int numeroResponsableTipo){
		List<ResponsableTipo> ret = new ArrayList<ResponsableTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM ResponsableTipo a ";
			Query<ResponsableTipo> criteria = session.createQuery(query,ResponsableTipo.class);
			
			criteria.setFirstResult(((pagina-1)*(numeroResponsableTipo)));
			criteria.setMaxResults(numeroResponsableTipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("2", ResponsableTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalResponsableTipo(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(t.id) FROM ResponsableTipo t ";
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("3", ResponsableTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalResponsablesTipos(String filtro_nombre, String filtro_descripcion, String filtro_usuario_creo,
			String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM ResponsableTipo p WHERE p.estado=1 ";
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
			CLogger.write("4", ResponsableTipo.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ResponsableTipo> getResponsableTipoPagina(int pagina, int numeroresponsabletipo,
			String filtro_nombre, String filtro_descripcion,String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<ResponsableTipo> ret = new ArrayList<ResponsableTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT p FROM ResponsableTipo p WHERE p.estado = 1";
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
			Query<ResponsableTipo> criteria = session.createQuery(query,ResponsableTipo.class);
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
	
	public static boolean guardarResponsableTipo(ResponsableTipo responsableTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(responsableTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", ResponsableTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarResponsableTipo(ResponsableTipo responsabletipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			responsabletipo.setEstado(0);
			session.beginTransaction();
			session.update(responsabletipo);
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