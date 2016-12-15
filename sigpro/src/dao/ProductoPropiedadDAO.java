package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProductoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class ProductoPropiedadDAO {
	static class EstructuraPojo {
		Integer id;
		String nombre;
		String descripcion;
		Integer idTipo;
		String tipo;
	}

	public static ProductoPropiedad getProductoPropiedad(Integer codigo) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProductoPropiedad ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProductoPropiedad> criteria = builder.createQuery(ProductoPropiedad.class);
			Root<ProductoPropiedad> root = criteria.from(ProductoPropiedad.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), codigo));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("1", ProductoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardar(Integer codigo, String nombre, String descripcion, String usuario, Integer tipo) {

		ProductoPropiedad pojo = getProductoPropiedad(codigo);
		boolean ret = false;

		if (pojo == null) {
			pojo = new ProductoPropiedad();
			pojo.setNombre(nombre);
			pojo.setDescripcion(descripcion);
			pojo.setEstado(1);
			pojo.setUsuarioCreo(usuario);
			pojo.setFechaCreacion(new Date());
			
			pojo.setProdtipoPropiedads(null);
			pojo.setProductoPropiedadValors(null);
			
			pojo.setDatoTipo(DatoTipoDAO.getDatoTipo(tipo));

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("2", ProductoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean actualizar(Integer codigo, String nombre, String descripcion, String usuario, Integer tipo) {

		ProductoPropiedad pojo = getProductoPropiedad(codigo);
		boolean ret = false;

		if (pojo != null) {
			pojo.setNombre(nombre);
			pojo.setDescripcion(descripcion);
			pojo.setUsuarioActualizo(usuario);
			pojo.setFechaActualizacion(new Date());
			
			pojo.setDatoTipo(DatoTipoDAO.getDatoTipo(tipo));

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("3", ProductoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean eliminar(Integer codigo, String usuario) {

		ProductoPropiedad pojo = getProductoPropiedad(codigo);
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
				CLogger.write("4", ProductoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static List<ProductoPropiedad> getPagina(int pagina, int registros) {
		List<ProductoPropiedad> ret = new ArrayList<ProductoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<ProductoPropiedad> criteria = session.createQuery("SELECT e FROM ProductoPropiedad e where e.estado = 1", ProductoPropiedad.class);
			criteria.setFirstResult(((pagina - 1) * (registros)));
			criteria.setMaxResults(registros);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("5", ProductoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros) {
		String jsonEntidades = "";

		List<ProductoPropiedad> pojos = getPagina(pagina, registros);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (ProductoPropiedad pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getNombre();
			estructuraPojo.descripcion = pojo.getDescripcion();
			estructuraPojo.idTipo = pojo.getDatoTipo().getId();
			estructuraPojo.tipo = pojo.getDatoTipo().getNombre();

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("productoPropiedades", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static Long getTotal() {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Long> conteo = session.createQuery("SELECT count(e.id) FROM ProductoPropiedad e  where e.estado = 1", Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", ProductoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

}
