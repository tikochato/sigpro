package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Producto;
import pojo.ProductoUsuario;
import pojo.ProductoUsuarioId;
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
			 ret = criteria.getSingleResult();;
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
			session.saveOrUpdate(producto);
			ProductoUsuario pu = new ProductoUsuario(new ProductoUsuarioId(producto.getId(), producto.getUsuarioCreo()), producto
					, producto.getUsuarioCreo(), null, new Date(), null);
			session.saveOrUpdate(pu);
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
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
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
	
	public static Producto getProductoInicial(Integer componenteId, String usuario){
		Producto ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "FROM Producto p where p.estado=1 and p.orden=1 and p.componente.id=:componenteId and p.usuarioCreo=:usuario";
			Query<Producto> criteria = session.createQuery(query, Producto.class);
			criteria.setParameter("componenteId", componenteId);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getSingleResult();
		}catch(Throwable e){
			CLogger.write("11", ProductoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Producto getProductoFechaMaxima(Integer componenteId, String usuario){
		Producto ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "FROM Producto p where p.estado=1 and p.componente.id=:componenteId and p.usuarioCreo=:usuario order by p.fechaFin desc";
			Query<Producto> criteria = session.createQuery(query, Producto.class);
			criteria.setMaxResults(1);
			criteria.setParameter("componenteId", componenteId);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getSingleResult();
		}catch(Throwable e){
			CLogger.write("12", ProductoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
}
