package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProductoTipo;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class ProductoTipoDAO {
	static class EstructuraPojo {
		Integer id;
		String nombre;
		String descripcion;
	}

	public static ProductoTipo getProductoTipo(Integer codigo) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProductoTipo ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProductoTipo> criteria = builder.createQuery(ProductoTipo.class);
			Root<ProductoTipo> root = criteria.from(ProductoTipo.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), codigo));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("1", ProductoTipoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardar(Integer codigo, String nombre, String descripcion, String usuario) {

		ProductoTipo pojo = getProductoTipo(codigo);
		boolean ret = false;

		if (pojo == null) {
			pojo = new ProductoTipo();
			pojo.setNombre(nombre);
			pojo.setDescripcion(descripcion);
			pojo.setEstado(1);
			pojo.setUsuarioCreo(usuario);
			pojo.setFechaCreacion(new Date());
			
			pojo.setProdtipoPropiedads(null);
			pojo.setProductos(null);

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("2", ProductoTipoDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean actualizar(Integer codigo, String nombre, String descripcion, String usuario) {

		ProductoTipo pojo = getProductoTipo(codigo);
		boolean ret = false;

		if (pojo != null) {
			pojo.setNombre(nombre);
			pojo.setDescripcion(descripcion);
			pojo.setUsuarioActualizo(usuario);
			pojo.setFechaActualizacion(new Date());

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("3", ProductoTipoDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean eliminar(Integer codigo, String usuario) {

		ProductoTipo pojo = getProductoTipo(codigo);
		boolean ret = false;

		if (pojo != null) {
			pojo.setUsuarioActualizo(usuario);
			pojo.setFechaActualizacion(new Date());
			pojo.setEstado(0);

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("4", ProductoTipoDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static List<ProductoTipo> getPagina(int pagina, int registros) {
		List<ProductoTipo> ret = new ArrayList<ProductoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<ProductoTipo> criteria = session.createQuery("SELECT e FROM ProductoTipo e where e.estado = 1", ProductoTipo.class);
			criteria.setFirstResult(((pagina - 1) * (registros)));
			criteria.setMaxResults(registros);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("5", ProductoTipoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros) {
		String jsonEntidades = "";

		List<ProductoTipo> pojos = getPagina(pagina, registros);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (ProductoTipo pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getNombre();
			estructuraPojo.descripcion = pojo.getDescripcion();

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("productoTipos", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static Long getTotal() {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Long> conteo = session.createQuery("SELECT count(e.id) FROM ProductoTipo e  where e.estado = 1", Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", ProductoTipoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

}
