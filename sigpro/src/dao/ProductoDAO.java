package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Producto;
import pojo.ProductoUsuario;
import pojo.ProductoUsuarioId;
import pojo.Usuario;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProductoDAO {


	public static List<Producto> getProductos(String usuario) {
		List<Producto> ret = new ArrayList<Producto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Producto> criteria = session.createQuery("FROM Producto p where p.id in (SELECT u.id.productoid from ProductoUsuario u where u.id.usuario=:usuario )", Producto.class);
			criteria.setParameter("usuario", usuario);
			ret =   criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("1", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static Producto getProductoPorId(int id, String usuario) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Producto ret = null;
		try {
			Query<Producto> criteria = session.createQuery("FROM Producto where id=:id AND id in (SELECT u.id.productoid from ProductoUsuario u where u.id.usuario=:usuario )", Producto.class);
			criteria.setParameter("id", id);
			criteria.setParameter("usuario", usuario);
			List<Producto> lista = criteria.getResultList();
			ret = !lista.isEmpty() ? lista.get(0) : null;
		} catch (Throwable e) {
			CLogger.write("2", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardarProducto(Producto producto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			producto.setNivel(2);
			session.saveOrUpdate(producto);
			Usuario usu = UsuarioDAO.getUsuario( producto.getUsuarioCreo());
			ProductoUsuario pu = new ProductoUsuario(new ProductoUsuarioId(producto.getId(), producto.getUsuarioCreo()), producto,usu
					, producto.getUsuarioCreo(), null, new Date(), null);
			session.saveOrUpdate(pu);
			if(!producto.getUsuarioCreo().equals("admin")){
				ProductoUsuario pu_admin = new ProductoUsuario(new ProductoUsuarioId(producto.getId(), "admin"), producto,usu
						, producto.getUsuarioCreo(), null, new Date(), null);
				session.saveOrUpdate(pu_admin);
			}
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("3", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean eliminarProducto(Producto Producto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Producto.setEstado(0);
			session.beginTransaction();
			session.update(Producto);
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("4", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean eliminarTotalProducto(Producto Producto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			session.delete(Producto);
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("5", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static List<Producto> getProductosPagina(int pagina, int numeroProductos,Integer componenteid, 
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion, String columna_ordenada, 
			String orden_direccion,String usuario) {
		List<Producto> ret = new ArrayList<Producto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			
			String query = "SELECT p FROM Producto p WHERE p.estado = 1 "
					+ (componenteid!=null && componenteid > 0 ? "AND p.componente.id = :idComp " : "");
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.productoid from ProductoUsuario u where u.id.usuario=:usuario )");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<Producto> criteria = session.createQuery(query,Producto.class);
			criteria.setParameter("usuario", usuario);
			if (componenteid!=null && componenteid>0){
				criteria.setParameter("idComp", componenteid);
			}
			criteria.setFirstResult(((pagina - 1) * (numeroProductos)));
			criteria.setMaxResults(numeroProductos);
			
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("6", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static Long getTotalProductos(Integer componenteid, String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String usuario) {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			
			String query = "SELECT count(p.id) FROM Producto p WHERE p.estado = 1 "
					+ (componenteid!=null && componenteid > 0 ? "AND p.componente.id = :idComp " : "");
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.productoid from ProductoUsuario u where u.id.usuario=:usuario )");
			
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("usuario", usuario);
			if (componenteid!=null && componenteid > 0){
				conteo.setParameter("idComp", componenteid);
			}
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

		public static boolean eliminar(Integer productoId, String usuario) {
		boolean ret = false;

		Producto pojo = getProductoPorId(productoId,usuario);

		if (pojo != null) {
			pojo.setEstado(0);
			pojo.setOrden(null);
			Session session = CHibernateSession.getSessionFactory().openSession();

			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();

				ret = true;

			} catch (Throwable e) {
				CLogger.write("8", ProductoDAO.class, e);
			} finally {
				session.close();
			}
		}
		return ret;
	}
	
	public static Producto getProductoPorId(int id) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Producto ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Producto> criteria = builder.createQuery(Producto.class);
			Root<Producto> root = criteria.from(Producto.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id )));
			List<Producto> lista = session.createQuery( criteria ).getResultList();
			ret = !lista.isEmpty() ? lista.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("9", ProductoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Producto> getProductosPorProyecto(Integer idProyecto,String usuario) {
		List<Producto> ret = new ArrayList<Producto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			String query = String.join(" ", "select t.*"
					,"from (",
					"SELECT pr.* FROM producto pr JOIN componente c ON c.id = pr.componenteid "
					,"JOIN proyecto p ON p.id = c.proyectoid"
					,"where p.id = :idProy) as t"
					,usuario!=null && usuario.length()>0 ? 
					 "join producto_usuario pu on pu.productoid = t.id where pu.usuario = :usuario ":"",
					 usuario!=null && usuario.length()>0 ? "and" : "where", "t.estado = 1");
			
			
			Query<Producto> criteria = session.createNativeQuery(query,Producto.class);
			criteria.setParameter("idProy", idProyecto);
			if (usuario !=null && usuario.length()>0)
				criteria.setParameter("usuario", usuario);
			ret =   criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("10", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static Producto getProductoInicial(Integer componenteId, String usuario, Session session){
		Producto ret = null;
		try{
			String query = "FROM Producto p where p.estado=1 and p.orden=1 and p.componente.id=:componenteId and p.usuarioCreo=:usuario";
			Query<Producto> criteria = session.createQuery(query, Producto.class);
			criteria.setParameter("componenteId", componenteId);
			criteria.setParameter("usuario", usuario);
			List<Producto> lista = criteria.getResultList();
			ret = !lista.isEmpty() ? lista.get(0) : null;
		}catch(Throwable e){
			CLogger.write("11", ProductoDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static Producto getProductoFechaMaxima(Integer componenteId, String usuario, Session session){
		Producto ret = null;
		try{
			String query = "FROM Producto p where p.estado=1 and p.componente.id=:componenteId and p.usuarioCreo=:usuario order by p.fechaFin desc";
			Query<Producto> criteria = session.createQuery(query, Producto.class);
			criteria.setMaxResults(1);
			criteria.setParameter("componenteId", componenteId);
			criteria.setParameter("usuario", usuario);
			List<Producto> lista = criteria.getResultList();
			ret = !lista.isEmpty() ? lista.get(0) : null;
		}catch(Throwable e){
			CLogger.write("12", ProductoDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static List<Producto> getProductosOrden(Integer componenteid, String usuario, Session session) {
		List<Producto> ret = new ArrayList<Producto>();
		try {
			
			String query = "SELECT p FROM Producto p WHERE p.estado = 1 AND p.componente.id = :componenteid ";
			query = String.join("", query, " AND p.id in (SELECT u.id.productoid from ProductoUsuario u where u.id.usuario=:usuario )");
			
			Query<Producto> criteria = session.createQuery(query,Producto.class);
			criteria.setParameter("usuario", usuario);
			criteria.setParameter("componenteid", componenteid);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("13", ProductoDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		} 
		return ret;
	}
	
	public static Producto getProductoPorIdOrden(int id, String usuario, Session session) {
		Producto ret = null;
		try {
			Query<Producto> criteria = session.createQuery("FROM Producto where id=:id AND id in (SELECT u.id.productoid from ProductoUsuario u where u.id.usuario=:usuario )", Producto.class).setLockMode(LockModeType.PESSIMISTIC_READ);
			criteria.setParameter("id", id);
			criteria.setParameter("usuario", usuario);
			List<Producto> lista = criteria.getResultList();
			ret = !lista.isEmpty() ? lista.get(0) : null;
		} catch (Throwable e) {
			CLogger.write("14", ProductoDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarProductoOrden(Producto producto, Session session) {
		boolean ret = false;
		try {
			session.saveOrUpdate(producto);
			session.flush();
			session.clear();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("15", ProductoDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
}
