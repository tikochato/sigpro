package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import pojo.ProdtipoPropiedad;
import pojo.ProdtipoPropiedadId;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class ProdTipoPropiedadDAO {

	static class EstructuraPojo {
		Integer idTipo;
		String tipo;
		Integer idPropiedad;
		String propiedad;
		Integer idPropiedadTipo;
		String propiedadTipo;
		String estado;
	}

	public static ProdtipoPropiedad getProdtipoPropiedad(Integer codigoTipo, Integer codigoPropiedad) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProdtipoPropiedad ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProdtipoPropiedad> criteria = builder.createQuery(ProdtipoPropiedad.class);
			Root<ProdtipoPropiedad> root = criteria.from(ProdtipoPropiedad.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new ProdtipoPropiedadId(codigoTipo, codigoPropiedad)));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("1", ProdTipoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardar(Integer codigoTipo, Integer codigoPropiedad, String usuario) {

		ProdtipoPropiedad pojo = getProdtipoPropiedad(codigoTipo, codigoPropiedad);
		boolean ret = false;

		if (pojo == null) {
			pojo = new ProdtipoPropiedad();
			pojo.setId(new ProdtipoPropiedadId(codigoTipo, codigoPropiedad));
			pojo.setProductoTipo(ProductoTipoDAO.getProductoTipo(codigoTipo));
			pojo.setProductoPropiedad(ProductoPropiedadDAO.getProductoPropiedad(codigoPropiedad));
			pojo.setUsuarioCreo(usuario);
			pojo.setFechaCreacion(new Date());

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("2", ProdTipoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean actualizar(Integer codigoTipo, Integer codigoPropiedad, String nombre, String descripcion,
			String usuario) {

		ProdtipoPropiedad pojo = getProdtipoPropiedad(codigoTipo, codigoPropiedad);
		boolean ret = false;

		if (pojo != null) {

			pojo.setId(new ProdtipoPropiedadId(codigoTipo, codigoPropiedad));
			pojo.setProductoTipo(ProductoTipoDAO.getProductoTipo(codigoTipo));
			pojo.setProductoPropiedad(ProductoPropiedadDAO.getProductoPropiedad(codigoPropiedad));

			pojo.setUsuarioActualizo(usuario);
			pojo.setFechaActualizacion(new Date());

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("3", ProdTipoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean eliminar(Integer codigoTipo, Integer codigoPropiedad, String usuario) {

		ProdtipoPropiedad pojo = getProdtipoPropiedad(codigoTipo, codigoPropiedad);
		boolean ret = false;

		if (pojo != null) {

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.delete(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("4", ProdTipoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static List<ProdtipoPropiedad> getPagina(int pagina, int registros, Integer codigoTipo) {
		List<ProdtipoPropiedad> ret = new ArrayList<ProdtipoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<ProdtipoPropiedad> criteria = session.createQuery(
					"SELECT e FROM ProdtipoPropiedad e where e.productoTipoid = " + codigoTipo,
					ProdtipoPropiedad.class);
			criteria.setFirstResult(((pagina - 1) * (registros)));
			criteria.setMaxResults(registros);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("5", ProdTipoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros, Integer codigoTipo) {
		String jsonEntidades = "";

		List<ProdtipoPropiedad> pojos = getPagina(pagina, registros, codigoTipo);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (ProdtipoPropiedad pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.idTipo = pojo.getProductoTipo().getId();
			estructuraPojo.tipo = pojo.getProductoTipo().getNombre();
			estructuraPojo.idPropiedad = pojo.getProductoPropiedad().getId();
			estructuraPojo.propiedad = pojo.getProductoPropiedad().getNombre();
			estructuraPojo.idPropiedadTipo = pojo.getProductoPropiedad().getDatoTipo().getId();
			estructuraPojo.propiedadTipo = pojo.getProductoPropiedad().getDatoTipo().getNombre();
			estructuraPojo.estado = "C";

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("productoTipos", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static Long getTotal() {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Long> conteo = session.createQuery("SELECT count(e.id) FROM ProdtipoPropiedad e", Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", ProdTipoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static List<ProdtipoPropiedad> getTipoPropiedades(Integer codigoTipo) {
		List<ProdtipoPropiedad> ret = new ArrayList<ProdtipoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<ProdtipoPropiedad> criteria = session.createQuery(
					"SELECT e FROM ProdtipoPropiedad e where e.id.productoTipoid = :id", ProdtipoPropiedad.class);
			criteria.setParameter("id", codigoTipo);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("8", ProdTipoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(Integer codigoTipo) {
		String jsonEntidades = "";

		List<ProdtipoPropiedad> pojos = getTipoPropiedades(codigoTipo);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (ProdtipoPropiedad pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.idTipo = pojo.getProductoTipo().getId();
			estructuraPojo.tipo = pojo.getProductoTipo().getNombre();
			estructuraPojo.idPropiedad = pojo.getProductoPropiedad().getId();
			estructuraPojo.propiedad = pojo.getProductoPropiedad().getNombre();
			estructuraPojo.idPropiedadTipo = pojo.getProductoPropiedad().getDatoTipo().getId();
			estructuraPojo.propiedadTipo = pojo.getProductoPropiedad().getDatoTipo().getNombre();
			estructuraPojo.estado = "C";

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("productoTipos", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static boolean persistirPropiedades(Integer idTipo, String propiedades, String usuario) {
		boolean ret = false;

		Gson gson = new Gson();

		List<EstructuraPojo> pojos = gson.fromJson(propiedades, new TypeToken<List<EstructuraPojo>>() {
		}.getType());

		for (EstructuraPojo pojo : pojos) {
			
			if(pojo.estado.equalsIgnoreCase("N")){
				ret = guardar(idTipo, pojo.idPropiedad, usuario);
			}else if(pojo.estado.equalsIgnoreCase("E")){
				ret = eliminar(pojo.idTipo, pojo.idPropiedad, usuario);
			}
		}

		return ret;
	}

}
