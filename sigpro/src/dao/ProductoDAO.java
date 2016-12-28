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

	public static List<Producto> getProductosPagina(int pagina, int numeroProductos) {
		List<Producto> ret = new ArrayList<Producto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Producto> criteria = session.createQuery("SELECT p FROM Producto p WHERE p.estado = 1",
					Producto.class);
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

	public static Long getTotalProductos() {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM Producto p WHERE p.estado = 1",
					Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros) {
		String jsonEntidades = "";

		List<Producto> pojos = getProductosPagina(pagina, registros);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (Producto pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getName();
			estructuraPojo.descripcion = pojo.getDescripcion();

			estructuraPojo.idComponente = pojo.getComponente().getId();
			estructuraPojo.componente = pojo.getComponente().getNombre();

			estructuraPojo.idProducto = pojo.getProducto().getId();
			estructuraPojo.producto = pojo.getProducto().getName();

			estructuraPojo.idProductoTipo = pojo.getProductoTipo().getId();
			estructuraPojo.productoTipo = pojo.getProductoTipo().getNombre();

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("productos", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static boolean guardar(String nombre, String descripcion, Integer componente, Integer producto, Integer tipo,
			String formulariosItemValor, String metas, String objetoFormularios, String objetosRecursos,
			String objetoRiesgos, String productoPropiedadValor, String Producto, String usuario) {
		boolean ret = false;
		Producto prod = new Producto();
		prod.setName(nombre);
		prod.setDescripcion(descripcion);

		prod.setComponente(ComponenteDAO.getComponentePorId(componente));

		if (producto != null)
			prod.setProducto(ProductoDAO.getProductoPorId(producto));
		else
			prod.setProducto(null);

		prod.setProductoTipo(ProductoTipoDAO.getProductoTipo(tipo));

		prod.setFormularioItemValors(null);
		prod.setMetas(null);
		prod.setObjetoFormularios(null);
		prod.setObjetoRecursos(null);
		prod.setObjetoRiesgos(null);
		prod.setProductoPropiedadValors(null);
		prod.setProductos(null);

		prod.setUsuarioCreo(usuario);
		prod.setFechaCreacion(new Date());

		Session session = CHibernateSession.getSessionFactory().openSession();
		Integer id = null;
		try {
			session.beginTransaction();
			id = (Integer) session.save(Producto);
			session.getTransaction().commit();
			
			ret = true;
			
			
		} catch (Throwable e) {
			CLogger.write("3", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
}
