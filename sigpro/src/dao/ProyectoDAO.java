package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Proyecto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProyectoDAO implements java.io.Serializable  {


	private static final long serialVersionUID = 1L;

	public static List<Proyecto> getProyectos(String usuario){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto p where p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario )", Proyecto.class);
			criteria.setParameter("usuario", usuario);
			ret =   (List<Proyecto>)criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", Proyecto.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}

	public static boolean guardarProyecto(Proyecto proyecto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(proyecto);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Proyecto getProyectoPorId(int id, String usuario){

		Session session = CHibernateSession.getSessionFactory().openSession();
		Proyecto ret = null;
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto where id=:id AND id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario )", Proyecto.class);
			criteria.setParameter("id", id);
			criteria.setParameter("usuario", usuario);
			 ret = (Proyecto) criteria.getSingleResult();;
		}
		catch(Throwable e){
			CLogger.write("3", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarProyecto(Proyecto proyecto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			proyecto.setEstado(0);
			session.beginTransaction();
			session.update(proyecto);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalProyectos(String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM Proyecto p WHERE p.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario )");
			Query<Long> criteria = session.createQuery(query,Long.class);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("5", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<Proyecto> getProyectosPagina(int pagina, int numeroproyecto,
			String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion, String usuario){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT p FROM Proyecto p WHERE p.estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario )");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Proyecto> criteria = session.createQuery(query,Proyecto.class);
			criteria.setParameter("usuario", usuario);
			criteria.setFirstResult(((pagina-1)*(numeroproyecto)));
			criteria.setMaxResults(numeroproyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}


}
