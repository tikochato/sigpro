package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Entidad;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

class EstructuraEntidad {
	Integer ejercicio;
	Integer entidad;
	String nombre;
	String abreviatura;
}

public class EntidadDAO {

	public static List<Entidad> getEntidades(int ejercicio) {
		List<Entidad> entidades = new ArrayList<Entidad>();

		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Entidad> criteria = builder.createQuery(Entidad.class);
			Root<Entidad> root = criteria.from(Entidad.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id").get("ejercicio"), ejercicio));
			entidades = session.createQuery(criteria).getResultList();

		} catch (Throwable e) {
			CLogger.write("1", EntidadDAO.class, e);
		} finally {
			session.close();
		}

		return entidades;
	}

	public static Entidad getEntidad(Integer entidad, Integer ejercicio) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Entidad ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Entidad> criteria = builder.createQuery(Entidad.class);
			Root<Entidad> root = criteria.from(Entidad.class);
			criteria.select(root);
			criteria.where(builder.and(builder.equal(root.get("id.entidad"), entidad), builder.equal(root.get("id.ejercicio"), ejercicio)));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("2", EntidadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardarEntidad(int entidad, int ejercicio, String nombre, String abreviatura) {

		Entidad nuevaEntidad = getEntidad(entidad, ejercicio);
		boolean ret = false;

		if (nuevaEntidad == null) {
			nuevaEntidad = new Entidad();
			//nuevaEntidad.setEntidad(entidad);
			nuevaEntidad.setNombre(nombre);
			nuevaEntidad.setAbreviatura(abreviatura);
			nuevaEntidad.setUnidadEjecutoras(null);

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(nuevaEntidad);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("3", EntidadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean actualizarEntidad(int entidad, int ejercicio,String abreviatura) {

		Entidad existeEntidad = getEntidad(entidad, ejercicio);
		boolean ret = false;

		if (existeEntidad != null) {

			existeEntidad.setAbreviatura(abreviatura);

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.update(existeEntidad);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("4", EntidadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static List<Entidad> getEntidadesPagina(int pagina, int registros,
			String filtro_entidad, String filtro_nombre, String filtro_abreviatura,
			String columna_ordenada, String orden_direccion) {
		List<Entidad> ret = new ArrayList<Entidad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			String query = "SELECT e FROM Entidad e";
			String query_a="";
			if(filtro_entidad!=null && filtro_entidad.trim().length()>0)
				query_a = String.join("",query_a, " str(e.entidad) LIKE '%",filtro_nombre,"%' ");
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " e.nombre LIKE '%", filtro_nombre,"%' ");
			if(filtro_abreviatura!=null && filtro_abreviatura.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " e.abreviatura LIKE '%", filtro_abreviatura,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","WHERE (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Entidad> criteria = session.createQuery(query , Entidad.class);
			criteria.setFirstResult(((pagina - 1) * (registros)));
			criteria.setMaxResults(registros);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("5", EntidadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJsonEntidades(int pagina, int registros,
			String filtro_entidad, String filtro_nombre, String filtro_abreviatura,
			String columna_ordenada, String orden_direccion) {
		String jsonEntidades = "";

		List<Entidad> entidadesPojo = getEntidadesPagina(pagina, registros, filtro_entidad, filtro_nombre, filtro_abreviatura, columna_ordenada, orden_direccion);

		List<EstructuraEntidad> entidades = new ArrayList<EstructuraEntidad>();

		for (Entidad entidadPojo : entidadesPojo) {
			EstructuraEntidad entidad = new EstructuraEntidad();
			entidad.entidad = entidadPojo.getId().getEntidad();
			entidad.nombre = entidadPojo.getNombre();
			entidad.abreviatura = entidadPojo.getAbreviatura();

			entidades.add(entidad);
		}

		jsonEntidades = Utils.getJSonString("entidades", entidades);

		return jsonEntidades;
	}
	
	public static String getJsonEntidadesPorEjercicio(int ejercicio) {
		String jsonEntidades = "";

		List<Entidad> entidadesPojo = getEntidades(ejercicio);

		List<EstructuraEntidad> entidades = new ArrayList<EstructuraEntidad>();

		for (Entidad entidadPojo : entidadesPojo) {
			EstructuraEntidad entidad = new EstructuraEntidad();
			entidad.ejercicio = entidadPojo.getId().getEjercicio();
			entidad.entidad = entidadPojo.getId().getEntidad();
			entidad.nombre = entidadPojo.getNombre();
			entidad.abreviatura = entidadPojo.getAbreviatura();

			entidades.add(entidad);
		}

		jsonEntidades = Utils.getJSonString("entidades", entidades);

		return jsonEntidades;
	}

	public static Long getTotalEntidades(String filtro_entidad, String filtro_nombre, String filtro_abreviatura) {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			String query = "SELECT count(e.entidad) FROM Entidad e ";
			String query_a="";
			if(filtro_entidad!=null && filtro_entidad.trim().length()>0)
				query_a = String.join("",query_a, " str(e.entidad) LIKE '%",filtro_nombre,"%' ");
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " e.nombre LIKE '%", filtro_nombre,"%' ");
			if(filtro_abreviatura!=null && filtro_abreviatura.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " e.abreviatura LIKE '%", filtro_abreviatura,"%' ");
			query = query_a.length()>0 ?  String.join("",query, " WHERE ", query_a) : query;
			Query<Long> conteo = session.createQuery(query, Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", EntidadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

}
