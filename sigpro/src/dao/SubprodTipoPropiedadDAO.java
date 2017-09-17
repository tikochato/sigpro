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

import pojo.SubprodtipoPropiedad;
import pojo.SubprodtipoPropiedadId;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class SubprodTipoPropiedadDAO {

	static class EstructuraPojo {
		Integer idTipo;
		String tipo;
		Integer idPropiedad;
		String propiedad;
		Integer idPropiedadTipo;
		String propiedadTipo;
		String estado;
	}

	public static SubprodtipoPropiedad getSubprodtipoPropiedad(Integer codigoTipo, Integer codigoPropiedad) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		SubprodtipoPropiedad ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<SubprodtipoPropiedad> criteria = builder.createQuery(SubprodtipoPropiedad.class);
			Root<SubprodtipoPropiedad> root = criteria.from(SubprodtipoPropiedad.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new SubprodtipoPropiedadId(codigoTipo, codigoPropiedad)));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("1", SubprodTipoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardar(Integer codigoTipo, Integer codigoPropiedad, String usuario) {

		SubprodtipoPropiedad pojo = getSubprodtipoPropiedad(codigoTipo, codigoPropiedad);
		boolean ret = false;

		if (pojo == null) {
			pojo = new SubprodtipoPropiedad();
			pojo.setId(new SubprodtipoPropiedadId(codigoTipo, codigoPropiedad));
			pojo.setSubproductoTipo(SubproductoTipoDAO.getSubproductoTipo(codigoTipo));
			pojo.setSubproductoPropiedad(SubproductoPropiedadDAO.getSubproductoPropiedad(codigoPropiedad));
			pojo.setUsuarioCreo(usuario);
			pojo.setFechaCreacion(new Date());

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("2", SubprodTipoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}else{
			ret = true;
		}

		return ret;
	}

	public static boolean actualizar(Integer codigoTipo, Integer codigoPropiedad, String nombre, String descripcion,
			String usuario) {

		SubprodtipoPropiedad pojo = getSubprodtipoPropiedad(codigoTipo, codigoPropiedad);
		boolean ret = false;

		if (pojo != null) {

			pojo.setId(new SubprodtipoPropiedadId(codigoTipo, codigoPropiedad));
			pojo.setSubproductoTipo(SubproductoTipoDAO.getSubproductoTipo(codigoTipo));
			pojo.setSubproductoPropiedad(SubproductoPropiedadDAO.getSubproductoPropiedad(codigoPropiedad));

			pojo.setUsuarioActualizo(usuario);
			pojo.setFechaActualizacion(new Date());

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("3", SubprodTipoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean eliminar(Integer codigoTipo, Integer codigoPropiedad, String usuario) {

		SubprodtipoPropiedad pojo = getSubprodtipoPropiedad(codigoTipo, codigoPropiedad);
		boolean ret = false;

		if (pojo != null) {

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.delete(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("4", SubprodTipoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static List<SubprodtipoPropiedad> getPagina(int pagina, int registros, Integer codigoTipo) {
		List<SubprodtipoPropiedad> ret = new ArrayList<SubprodtipoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<SubprodtipoPropiedad> criteria = session.createQuery(
					"SELECT e FROM SubprodtipoPropiedad e where e.productoTipoid = " + codigoTipo,
					SubprodtipoPropiedad.class);
			criteria.setFirstResult(((pagina - 1) * (registros)));
			criteria.setMaxResults(registros);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("5", SubprodTipoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros, Integer codigoTipo) {
		String jsonEntidades = "";

		List<SubprodtipoPropiedad> pojos = getPagina(pagina, registros, codigoTipo);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (SubprodtipoPropiedad pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.idTipo = pojo.getSubproductoTipo().getId();
			estructuraPojo.tipo = pojo.getSubproductoTipo().getNombre();
			estructuraPojo.idPropiedad = pojo.getSubproductoPropiedad().getId();
			estructuraPojo.propiedad = pojo.getSubproductoPropiedad().getNombre();
			estructuraPojo.idPropiedadTipo = pojo.getSubproductoPropiedad().getDatoTipo().getId();
			estructuraPojo.propiedadTipo = pojo.getSubproductoPropiedad().getDatoTipo().getNombre();
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
			Query<Long> conteo = session.createQuery("SELECT count(e.id) FROM SubprodtipoPropiedad e", Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", SubprodTipoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static List<SubprodtipoPropiedad> getTipoPropiedades(Integer codigoTipo) {
		List<SubprodtipoPropiedad> ret = new ArrayList<SubprodtipoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<SubprodtipoPropiedad> criteria = session.createQuery(
					"SELECT e FROM SubprodtipoPropiedad e where e.id.subproductoTipoid = :id", SubprodtipoPropiedad.class);
			criteria.setParameter("id", codigoTipo);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("8", SubprodTipoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(Integer codigoTipo) {
		String jsonEntidades = "";

		List<SubprodtipoPropiedad> pojos = getTipoPropiedades(codigoTipo);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (SubprodtipoPropiedad pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.idTipo = pojo.getSubproductoTipo().getId();
			estructuraPojo.tipo = pojo.getSubproductoTipo().getNombre();
			estructuraPojo.idPropiedad = pojo.getSubproductoPropiedad().getId();
			estructuraPojo.propiedad = pojo.getSubproductoPropiedad().getNombre();
			estructuraPojo.idPropiedadTipo = pojo.getSubproductoPropiedad().getDatoTipo().getId();
			estructuraPojo.propiedadTipo = pojo.getSubproductoPropiedad().getDatoTipo().getNombre();
			estructuraPojo.estado = "C";

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("subproductoTipos", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static boolean persistirPropiedades(Integer idTipo, String propiedades, String usuario) {
		boolean ret = true;

		Gson gson = new Gson();

		List<EstructuraPojo> pojos = gson.fromJson(propiedades, new TypeToken<List<EstructuraPojo>>() {
		}.getType());

		if (!pojos.isEmpty()){
			for (EstructuraPojo pojo : pojos) {
				
				if(pojo.estado.equalsIgnoreCase("N")){
					ret = guardar(idTipo, pojo.idPropiedad, usuario);
				}else if(pojo.estado.equalsIgnoreCase("E")){
					ret = eliminar(pojo.idTipo, pojo.idPropiedad, usuario);
				}
			}
		}else{
			ret = true;
		}

		return ret;
	}

}
