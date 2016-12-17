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
    Integer entidad;
    String nombre;
    String abreviatura;
}

public class EntidadDAO {

    public static List<Entidad> getEntidades() {
	List<Entidad> entidades = new ArrayList<Entidad>();

	Session session = CHibernateSession.getSessionFactory().openSession();
	try {
	    CriteriaBuilder builder = session.getCriteriaBuilder();

	    CriteriaQuery<Entidad> criteria = builder.createQuery(Entidad.class);
	    Root<Entidad> root = criteria.from(Entidad.class);
	    criteria.select(root);
	    entidades = session.createQuery(criteria).getResultList();

	}
	catch (Throwable e) {
	    CLogger.write("1", EntidadDAO.class, e);
	}
	finally {
	    session.close();
	}

	return entidades;
    }

    public static Entidad getEntidad(Integer entidad) {
	Session session = CHibernateSession.getSessionFactory().openSession();
	Entidad ret = null;
	try {
	    CriteriaBuilder builder = session.getCriteriaBuilder();

	    CriteriaQuery<Entidad> criteria = builder.createQuery(Entidad.class);
	    Root<Entidad> root = criteria.from(Entidad.class);
	    criteria.select(root);
	    criteria.where(builder.equal(root.get("entidad"), entidad));
	    ret = session.createQuery(criteria).getSingleResult();
	}
	catch (Throwable e) {
	    CLogger.write("2", EntidadDAO.class, e);
	}
	finally {
	    session.close();
	}
	return ret;
    }

    public static boolean guardarEntidad(int entidad, String nombre, String abreviatura) {

	Entidad nuevaEntidad = getEntidad(entidad);
	boolean ret = false;

	if (nuevaEntidad == null) {
	    nuevaEntidad = new Entidad();
	    nuevaEntidad.setEntidad(entidad);
	    nuevaEntidad.setNombre(nombre);
	    nuevaEntidad.setAbreviatura(abreviatura);
	    nuevaEntidad.setUnidadEjecutoras(null);

	    Session session = CHibernateSession.getSessionFactory().openSession();
	    try {
		session.beginTransaction();
		session.save(nuevaEntidad);
		session.getTransaction().commit();
		ret = true;
	    }
	    catch (Throwable e) {
		CLogger.write("3", EntidadDAO.class, e);
	    }
	    finally {
		session.close();
	    }
	}

	return ret;
    }

    public static boolean actualizarEntidad(int entidad, String abreviatura) {

	Entidad existeEntidad = getEntidad(entidad);
	boolean ret = false;

	if (existeEntidad != null) {

		existeEntidad.setAbreviatura(abreviatura);
		
	    Session session = CHibernateSession.getSessionFactory().openSession();
	    try {
		session.beginTransaction();
		session.update(existeEntidad);
		session.getTransaction().commit();
		ret = true;
	    }
	    catch (Throwable e) {
		CLogger.write("4", EntidadDAO.class, e);
	    }
	    finally {
		session.close();
	    }
	}

	return ret;
    }

    public static List<Entidad> getEntidadesPagina(int pagina, int registros) {
	List<Entidad> ret = new ArrayList<Entidad>();
	Session session = CHibernateSession.getSessionFactory().openSession();
	try {
	    Query<Entidad> criteria = session.createQuery("SELECT e FROM Entidad e", Entidad.class);
	    criteria.setFirstResult(((pagina
	                              - 1 )
	                             * (registros ) ));
	    criteria.setMaxResults(registros);
	    ret = criteria.getResultList();
	}
	catch (Throwable e) {
	    CLogger.write("5", EntidadDAO.class, e);
	}
	finally {
	    session.close();
	}
	return ret;
    }

    public static String getJsonEntidades(int pagina, int registros) {
	String jsonEntidades = "";

	List<Entidad> entidadesPojo = getEntidadesPagina(pagina, registros);

	List<EstructuraEntidad> entidades = new ArrayList<EstructuraEntidad>();

	for (Entidad entidadPojo : entidadesPojo) {
	    EstructuraEntidad entidad = new EstructuraEntidad();
	    entidad.entidad = entidadPojo.getEntidad();
	    entidad.nombre = entidadPojo.getNombre();
	    entidad.abreviatura = entidadPojo.getAbreviatura();

	    entidades.add(entidad);
	}

	jsonEntidades = Utils.getJSonString("entidades", entidades);

	return jsonEntidades;
    }

    public static Long getTotalEntidades() {
	Long ret = 0L;
	Session session = CHibernateSession.getSessionFactory().openSession();
	try {
	    Query<Long> conteo = session.createQuery("SELECT count(e.entidad) FROM Entidad e", Long.class);
	    ret = conteo.getSingleResult();
	}
	catch (Throwable e) {
	    CLogger.write("7", CooperanteDAO.class, e);
	}
	finally {
	    session.close();
	}
	return ret;
    }

}
