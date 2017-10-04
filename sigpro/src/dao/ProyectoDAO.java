package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.LockModeType;
import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.ProyectoUsuario;
import pojo.ProyectoUsuarioId;
import pojo.Subproducto;
import pojo.Usuario;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProyectoDAO implements java.io.Serializable  {


	private static final long serialVersionUID = 1L;

	public static List<Proyecto> getProyectos(String usuario){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto p where p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario ) and p.estado=1", Proyecto.class);
			criteria.setParameter("usuario", usuario);
			ret =   criteria.getResultList();
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
			if(proyecto.getId()==null || proyecto.getId()<1){
				session.saveOrUpdate(proyecto);
				session.flush();
				proyecto.setTreePath((10000000+proyecto.getId())+"");
			}
			session.saveOrUpdate(proyecto);
			Usuario usu = UsuarioDAO.getUsuario( proyecto.getUsuarioCreo());
			ProyectoUsuario pu = new ProyectoUsuario(new ProyectoUsuarioId(proyecto.getId(),proyecto.getUsuarioCreo()), proyecto,usu);
			session.saveOrUpdate(pu);
			if(!proyecto.getUsuarioCreo().equals("admin")){
				ProyectoUsuario pu_admin = new ProyectoUsuario(new ProyectoUsuarioId(proyecto.getId(),"admin"), proyecto,usu);
				session.saveOrUpdate(pu_admin);
			}
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
			 ret = criteria.getSingleResult();
		} catch (NoResultException e){
			
		}
		catch(Throwable e){
			CLogger.write("3", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static Proyecto getProyecto(int id){

		Session session = CHibernateSession.getSessionFactory().openSession();
		Proyecto ret = null;
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto where id=:id", Proyecto.class);
			criteria.setParameter("id", id);
			 ret = criteria.getSingleResult();
		} catch (NoResultException e){
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
		} catch (NoResultException e){
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
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) :
						String.join(" ", query, "ORDER BY fecha_creacion ASC");
			
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
	
	public static List<Proyecto> getProyectosPaginaDisponibles(int pagina, int numeroproyecto,
			String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion,String idsProyectos){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT p FROM Proyecto p WHERE p.estado = 1";
			if (idsProyectos!=null && idsProyectos.trim().length()>0)
				query = String.join("", query," AND p.id not in (" + idsProyectos + ")");
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<Proyecto> criteria = session.createQuery(query,Proyecto.class);
			criteria.setFirstResult(((pagina-1)*(numeroproyecto)));
			criteria.setMaxResults(numeroproyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("7", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalProyectosDisponibles(String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String idsProyectos){
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
			if(idsProyectos!=null && idsProyectos.trim().length()>0)
				query = String.join("", query, " AND p.id not in ("+idsProyectos + " )");
			Query<Long> criteria = session.createQuery(query,Long.class);
			ret = criteria.getSingleResult();
		} catch (NoResultException e){
		}
		catch(Throwable e){
			CLogger.write("8", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Proyecto> getProyectosPorPrograma(int idPrograma){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Proyecto> criteria = session.createQuery("select p from Proyecto p "
					+ "inner join p.programaProyectos pp "
					+ "where pp.estado = 1 "
					+ "and pp.id.programaid = :idProg", Proyecto.class);
			
			criteria.setParameter("idProg", idPrograma);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("9", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}

	public static List<Proyecto> getProyectosPorUnidadEjecutora(String usuario, int unidadEjecutoraId){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Proyecto> criteria = session.createQuery("select p from Proyecto p "
					+ "inner join p.unidadEjecutora pp "
					+ "where p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario ) "
					+ "and p.estado=1 and pp.unidadEjecutora=:unidadEjecutora", Proyecto.class);
			criteria.setParameter("usuario", usuario);
			criteria.setParameter("unidadEjecutora", unidadEjecutoraId);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("10", Proyecto.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}
	
	public static Proyecto getProyectoOrden(int id, Session session){
		Proyecto ret = null;
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto where id=:id", Proyecto.class).setLockMode(LockModeType.PESSIMISTIC_READ);
			criteria.setParameter("id", id);
			 ret = criteria.getSingleResult();
		} catch (NoResultException e){
		}
		catch(Throwable e){
			CLogger.write("11", ProyectoDAO.class, e);
		}
		return ret;
	}
	
	public static boolean guardarProyectoOrden(Proyecto proyecto, Session session){
		boolean ret = false;
		try{
			session.save(proyecto);
			session.flush();
			session.clear();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("12", ProyectoDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static List<Proyecto> getTodosProyectos(){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto p where p.estado=1", Proyecto.class);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("13", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Boolean calcularTreepah(Integer proyectoId){
		Boolean ret = true;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
			proyecto.setTreePath((10000000+proyecto.getId())+"");
			ProyectoDAO.guardarProyecto(proyecto);
			for(Componente componente : proyecto.getComponentes()){
				componente.setTreePath(proyecto.getTreePath()+""+(10000000+componente.getId()));
				ComponenteDAO.guardarComponente(componente);
				for(Producto producto:componente.getProductos()){
					producto.setTreePath(componente.getTreePath()+""+(10000000+producto.getId()));
					ProductoDAO.guardarProducto(producto);
					for(Subproducto subproducto:producto.getSubproductos()){
						subproducto.setTreePath(producto.getTreePath());
						SubproductoDAO.guardarSubproducto(subproducto);
					}
				}
			}
			Query<Actividad> criteria = session.createQuery("FROM Actividad a where a.treePath LIKE '"+(10000000+proyectoId)+"%'", Actividad.class);
			ArrayList<Actividad> actividades =   new ArrayList<Actividad>(criteria.getResultList());
			for(Actividad actividad:actividades){
				switch(actividad.getObjetoTipo()){
					case 1:
						actividad.setTreePath(proyecto.getTreePath()+""+(10000000+proyecto.getId()));
						break;
					case 2:
						Componente componente = ComponenteDAO.getComponente(actividad.getObjetoId());
						actividad.setTreePath(componente.getTreePath()+""+(10000000+actividad.getId()));
						break;
					case 3:
						Producto producto = ProductoDAO.getProductoPorId(actividad.getObjetoId());
						actividad.setTreePath(producto.getTreePath()+""+(10000000+actividad.getId()));
						break;
					case 4:
						Subproducto subproducto = SubproductoDAO.getSubproductoPorId(actividad.getObjetoId());
						actividad.setTreePath(subproducto.getTreePath()+""+(10000000+actividad.getId()));
						break;
					case 5:
						Actividad parent_actividad = ActividadDAO.getActividadPorId(actividad.getObjetoId());
						actividad.setTreePath(parent_actividad.getTreePath()+""+(10000000+actividad.getId()));
						break;
				}
			}
		}
		catch(Throwable e){
			CLogger.write("13", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
