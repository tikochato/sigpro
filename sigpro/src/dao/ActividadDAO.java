package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Actividad;
import pojo.ActividadUsuario;
import pojo.ActividadUsuarioId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ActividadDAO {
	public static List<Actividad> getActividads(String usuario){
		List<Actividad> ret = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Actividad> criteria = session.createQuery("FROM Actividad p where p.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )", Actividad.class);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Actividad getActividadPorId(int id, String usuario){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Actividad ret = null;
		try{
			Query<Actividad> criteria = session.createQuery("FROM Actividad where id=:id AND id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )", Actividad.class);
			criteria.setParameter("id", id);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarActividad(Actividad Actividad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(Actividad);
			ActividadUsuario au = new ActividadUsuario(new ActividadUsuarioId(Actividad.getId(), Actividad.getUsuarioCreo()),Actividad);
			session.saveOrUpdate(au);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarActividad(Actividad Actividad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Actividad.setEstado(0);
			session.beginTransaction();
			session.update(Actividad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarTotalActividad(Actividad Actividad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(Actividad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<Actividad> getActividadsPagina(int pagina, int numeroActividads,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion, String usuario){
		List<Actividad> ret = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT c FROM Actividad c WHERE estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = String.join("", query, " AND c.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario ) ");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Actividad> criteria = session.createQuery(query,Actividad.class);
			criteria.setParameter("usuario", usuario);
			criteria.setFirstResult(((pagina-1)*(numeroActividads)));
			criteria.setMaxResults(numeroActividads);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalActividads(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion, String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(c.id) FROM Actividad c WHERE c.estado=1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join("", query, " AND c.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario ) ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("usuario", usuario);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}


	public static List<Actividad> getActividadsPaginaPorObjeto(int pagina, int numeroActividads, int objetoId, int objetoTipo,String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion, String usuario){
		List<Actividad> ret = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM Actividad a WHERE a.estado = 1 AND a.objetoId = :objetoId AND a.objetoTipo = :objetoTipo ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " a.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " a.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(a.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND a.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query," ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Actividad> criteria = session.createQuery(query,Actividad.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			if (usuario!=null){
				criteria.setParameter("usuario", usuario);
			}
			criteria.setFirstResult(((pagina-1)*(numeroActividads)));
			criteria.setMaxResults(numeroActividads);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalActividadsPorObjeto(int objetoId, int objetoTipo, String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(a.id) FROM Actividad a WHERE a.estado=1 and a.objetoId=:objetoId and a.objetoTipo=:objetoTipo ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " a.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " a.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(a.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND a.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )");
			Query<Long> criteria = session.createQuery(query,Long.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("9", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
