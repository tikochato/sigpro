package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.DatoTipo;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class DatoTipoDAO {
	static class EstructuraComboPojo {
		Integer id;
		String nombre;
	}
	static class EstructuraPojo {
		Integer id;
		String nombre;
		String descripcion;
	}
	public static DatoTipo getDatoTipo(Integer codigo) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		DatoTipo ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<DatoTipo> criteria = builder.createQuery(DatoTipo.class);
			Root<DatoTipo> root = criteria.from(DatoTipo.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), codigo));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("1", DatoTipoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardar(Integer codigo, String nombre, String descripcion) {

		DatoTipo pojo = getDatoTipo(codigo);
		boolean ret = false;

		if (pojo == null) {
			pojo = new DatoTipo();
			pojo.setNombre(nombre);
			pojo.setDescripcion(descripcion);

			pojo.setComponentePropiedads(null);
			pojo.setProductoPropiedads(null);
			pojo.setProyectoPropiedads(null);

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("2", DatoTipoDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean actualizar(Integer codigo, String nombre, String descripcion) {

		DatoTipo pojo = getDatoTipo(codigo);
		boolean ret = false;

		if (pojo != null) {
			pojo.setNombre(nombre);
			pojo.setDescripcion(descripcion);

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("3", DatoTipoDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static List<DatoTipo> getPagina(int pagina, int registros) {
		List<DatoTipo> ret = new ArrayList<DatoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<DatoTipo> criteria = session.createQuery("SELECT e FROM DatoTipo e", DatoTipo.class);
			criteria.setFirstResult(((pagina - 1) * (registros)));
			criteria.setMaxResults(registros);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("5", DatoTipoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros) {
		String jsonEntidades = "";

		List<DatoTipo> pojos = getPagina(pagina, registros);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (DatoTipo pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getNombre();
			estructuraPojo.descripcion = pojo.getDescripcion();

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("datoTipos", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static Long getTotal() {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Long> conteo = session.createQuery("SELECT count(e.id) FROM DatoTipo e", Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", DatoTipoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static List<DatoTipo> getDatoTipos() {
		List<DatoTipo> ret = new ArrayList<DatoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<DatoTipo> criteria = builder.createQuery(DatoTipo.class);
			Root<DatoTipo> root = criteria.from(DatoTipo.class);
			criteria.select(root);
			ret = session.createQuery(criteria).getResultList();
		} catch (Throwable e) {
			CLogger.write("8", UnidadEjecutoraDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static String getJson() {
		String jsonEntidades = "";

		List<DatoTipo> pojos = getDatoTipos();

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (DatoTipo pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getNombre();
			estructuraPojo.descripcion = pojo.getDescripcion();

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("datoTipos", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static String getJsonCombo() {
		String jsonEntidades = "";

		List<DatoTipo> pojos = getDatoTipos();

		List<EstructuraComboPojo> listaEstructuraPojos = new ArrayList<EstructuraComboPojo>();

		for (DatoTipo pojo : pojos) {
			EstructuraComboPojo estructuraPojo = new EstructuraComboPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getNombre();

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("datoTipos", listaEstructuraPojos);

		return jsonEntidades;
	}
}
