package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Componente;
import pojo.ComponenteUsuario;
import pojo.ComponenteUsuarioId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ComponenteDAO {
	public static List<Componente> getComponentes(String usuario){
		List<Componente> ret = new ArrayList<Componente>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Componente> criteria = session.createQuery("FROM Componente p where estado = 1 AND p.id in (SELECT u.id.componenteid from ComponenteUsuario u where u.id.usuario=:usuario )", Componente.class);
			criteria.setParameter("usuario", usuario);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Componente getComponentePorId(int id, String usuario){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Componente ret = null;
		try{
			Query<Componente> criteria = session.createQuery("FROM Componente where id=:id AND id in (SELECT u.id.componenteid from ComponenteUsuario u where u.id.usuario=:usuario )", Componente.class);
			criteria.setParameter("id", id);
			criteria.setParameter("usuario", usuario);
			 ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarComponente(Componente Componente){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Componente.setNivel(1);
			session.saveOrUpdate(Componente);
			ComponenteUsuario cu = new ComponenteUsuario(new ComponenteUsuarioId(Componente.getId(), Componente.getUsuarioCreo()), Componente);
			session.saveOrUpdate(cu);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarComponente(Componente Componente){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Componente.setEstado(0);
			Componente.setOrden(null);
			session.beginTransaction();
			session.update(Componente);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", ComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarTotalComponente(Componente Componente){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(Componente);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", ComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<Componente> getComponentesPagina(int pagina, int numeroComponentes, String usuario){
		List<Componente> ret = new ArrayList<Componente>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Componente> criteria = session.createQuery("SELECT c FROM Componente c WHERE estado = 1 AND c.id in (SELECT u.id.componenteid from ComponenteUsuario u where u.id.usuario=:usuario )",Componente.class);
			criteria.setParameter("usuario", usuario);
			criteria.setFirstResult(((pagina-1)*(numeroComponentes)));
			criteria.setMaxResults(numeroComponentes);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", ComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalComponentes(String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(c.id) FROM Componente c WHERE c.estado=1 AND  c.id in (SELECT u.id.componenteid from ComponenteUsuario u where u.id.usuario=:usuario )",Long.class);
			conteo.setParameter("usuario", usuario);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", ComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<Componente> getComponentesPaginaPorProyecto(int pagina, int numeroComponentes, int proyectoId,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion, String columna_ordenada, String orden_direccion, String usuario){

		List<Componente> ret = new ArrayList<Componente>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{

			String query = "SELECT c FROM Componente c WHERE estado = 1 AND c.proyecto.id = :proyId ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query =String.join("",query," AND  c.id in (SELECT u.id.componenteid from ComponenteUsuario u where u.id.usuario=:usuario ) ");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Componente> criteria = session.createQuery(query,Componente.class);
			criteria.setParameter("proyId", proyectoId);
			criteria.setParameter("usuario", usuario);
			criteria.setFirstResult(((pagina-1)*(numeroComponentes)));
			criteria.setMaxResults(numeroComponentes);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", ComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}


	public static Long getTotalComponentesPorProyecto(int proyectoId,
			String filtro_nombre,String filtro_usuario_creo,
			String filtro_fecha_creacion, String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{

			String query = "SELECT count(c.id) FROM Componente c WHERE c.estado=1 AND c.proyecto.id = :proyId ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = String.join("", query, " AND  c.id in (SELECT u.id.componenteid from ComponenteUsuario u where u.id.usuario=:usuario )");
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("proyId", proyectoId);
			conteo.setParameter("usuario", usuario);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("9", ComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Componente getComponenteInicial(Integer proyectoId, String usuario, Session session){
		Componente ret = null;
		try{
			String query = "FROM Componente c where c.estado=1 and c.orden=1 and c.proyecto.id=:proyectoId and c.usuarioCreo=:usuario";
			Query<Componente> criteria = session.createQuery(query, Componente.class);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getSingleResult();
		}catch(Throwable e){
			CLogger.write("11", ComponenteDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static Componente getComponenteFechaMaxima(Integer proyectoId, String usuario, Session session){
		Componente ret = null;
		try{
			String query = "FROM Componente c where c.estado=1 and c.proyecto.id=:proyectoId and c.usuarioCreo=:usuario order by c.fechaFin desc";
			Query<Componente> criteria = session.createQuery(query, Componente.class);
			criteria.setMaxResults(1);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getSingleResult();
		}catch(Throwable e){
			CLogger.write("12", ComponenteDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static List<Componente> getComponentesOrden(Integer proyectoId, String usuario, Session session){
		List<Componente> ret = null;
		try{
			String query = String.join(" ", "SELECT c FROM Componente c where c.estado=1 and c.proyecto.id=:proyectoId");
			query = String.join(" ", query, "AND c.id in (SELECT u.id.componenteid from ComponenteUsuario u where u.id.usuario=:usuario)");
			Query<Componente> criteria = session.createQuery(query,Componente.class);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("13", ComponenteDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static Componente getComponentePorIdOrden(int id, String usuario, Session session){
		Componente ret = null;
		try{
			Query<Componente> criteria = session.createQuery("FROM Componente where id=:id AND id in (SELECT u.id.componenteid from ComponenteUsuario u where u.id.usuario=:usuario )", Componente.class);
			criteria.setParameter("id", id);
			criteria.setParameter("usuario", usuario);
			 ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("14", ComponenteDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarComponenteOrden(Componente Componente, Session session){
		boolean ret = false;
		try{
			session.saveOrUpdate(Componente);
			ComponenteUsuario cu = new ComponenteUsuario(new ComponenteUsuarioId(Componente.getId(), Componente.getUsuarioCreo()), Componente);
			
			session.flush();
			session.clear();
			session.saveOrUpdate(cu);
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("15", ComponenteDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
}
