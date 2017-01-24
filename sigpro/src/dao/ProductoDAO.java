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
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class ProductoDAO {

	
	static class EstructuraPojo {
		Integer id;
		String nombre;
		String descripcion;

		Integer idComponente;
		String componente;

		Integer idProducto;
		String producto;

		Integer idProductoTipo;
		String productoTipo;

		Integer estado;
	}

	public static List<Producto> getProductos() {
		List<Producto> ret = new ArrayList<Producto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Producto> criteria = builder.createQuery(Producto.class);
			Root<Producto> root = criteria.from(Producto.class);
			criteria.select(root).where(builder.equal(root.get("estado"), 1));
			ret = session.createQuery(criteria).getResultList();
		} catch (Throwable e) {
			CLogger.write("1", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static Producto getProductoPorId(int id) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Producto ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Producto> criteria = builder.createQuery(Producto.class);
			Root<Producto> root = criteria.from(Producto.class);
			criteria.select(root);
			criteria.where(builder.and(builder.equal(root.get("id"), id), builder.equal(root.get("estado"), 1)));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("2", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardarProducto(Producto Producto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(Producto);
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

	public static List<Producto> getProductosPagina(int pagina, int numeroProductos,Integer componenteid) {
		List<Producto> ret = new ArrayList<Producto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Producto> criteria = session.createQuery("SELECT p FROM Producto p WHERE p.estado = 1 "
					+ (componenteid!=null && componenteid > 0 ? "AND p.componente.id = :idComp " : ""),
					Producto.class);
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

	public static Long getTotalProductos(Integer componenteid) {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM Producto p WHERE p.estado = 1  "
					+ (componenteid!=null && componenteid > 0 ? "AND p.componente.id = :idComp " : ""),
					Long.class);
			
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

	public static String getJson(int pagina, int registros,Integer componenteid) {
		String jsonEntidades = "";

		List<Producto> pojos = getProductosPagina(pagina, registros,componenteid);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (Producto pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getNombre();
			estructuraPojo.descripcion = pojo.getDescripcion();

			if (pojo.getComponente() != null) {
				estructuraPojo.idComponente = pojo.getComponente().getId();
				estructuraPojo.componente = pojo.getComponente().getNombre();
			}

			if (pojo.getProductoTipo() != null) {
				estructuraPojo.idProductoTipo = pojo.getProductoTipo().getId();
				estructuraPojo.productoTipo = pojo.getProductoTipo().getNombre();
			}

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("productos", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static boolean guardar(String nombre, String descripcion, Integer componente, Integer productoPadre,
			Integer tipo, String propiedades, String actividades, String usuario) {
		boolean ret = false;

		Producto pojo = new Producto();

		pojo.setNombre(nombre);
		pojo.setDescripcion(descripcion);

		pojo.setComponente(componente > 0 ? ComponenteDAO.getComponentePorId(componente) : null);
		pojo.setProductoTipo(tipo > 0 ? ProductoTipoDAO.getProductoTipo(tipo) : null);

		pojo.setProductoPropiedadValors(null);
		pojo.setActividads(null);

		pojo.setUsuarioCreo(usuario);
		pojo.setFechaCreacion(new Date());

		pojo.setEstado(1);

		Session session = CHibernateSession.getSessionFactory().openSession();
		Integer productoid = null;
		try {
			session.beginTransaction();
			productoid = (Integer) session.save(pojo);
			session.getTransaction().commit();

			ret = ProductoPropiedadValorDAO.persistirValores(productoid, propiedades, usuario);

		} catch (Throwable e) {
			CLogger.write("8", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean actualizar(Integer productoId, String nombre, String descripcion, Integer componente,
			Integer productoPadre, Integer tipo, String propiedades, String actividades, String usuario) {
		boolean ret = false;

		Producto pojo = getProductoPorId(productoId);

		if (pojo != null) {
			pojo.setNombre(nombre);
			pojo.setDescripcion(descripcion);

			pojo.setComponente(componente != null ? ComponenteDAO.getComponentePorId(componente) : null);
			pojo.setProductoTipo(tipo != null ? ProductoTipoDAO.getProductoTipo(tipo) : null);

			pojo.setUsuarioActualizo(usuario);
			pojo.setFechaActualizacion(new Date());

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();

				ret = ProductoPropiedadValorDAO.persistirValores(productoId, propiedades, usuario);

			} catch (Throwable e) {
				CLogger.write("9", ProductoDAO.class, e);
			} finally {
				session.close();
			}
		}
		return ret;
	}

	public static boolean eliminar(Integer productoId, String usuario) {
		boolean ret = false;

		Producto pojo = getProductoPorId(productoId);

		if (pojo != null) {
			pojo.setEstado(0);

			Session session = CHibernateSession.getSessionFactory().openSession();

			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();

				ret = true;

			} catch (Throwable e) {
				CLogger.write("10", ProductoDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;

	}

}
