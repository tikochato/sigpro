package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Colaborador;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class ColaboradorDAO {
	static class EstructuraPojo {
		Integer id;
		String primerNombre;
		String segundoNombre;
		String otrosNombres;
		String primerApellido;
		String segundoApellido;
		String otrosApellidos;
		Long cui;
		Integer unidadEjecutora;
		String nombreUnidadEjecutora;
		String usuario;
	}

	public static Colaborador getColaborador(Integer codigo) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Colaborador ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Colaborador> criteria = builder.createQuery(Colaborador.class);
			Root<Colaborador> root = criteria.from(Colaborador.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), codigo));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("2", ColaboradorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardar(Integer codigo, String primerNombre, String segundoNombre, String otrosNombres,
			String primerApellido, String segundoApellido, String otrosApellidos, Long cui,
			Integer codigoUnidadEjecutora, String usuario) {

		Colaborador pojo = getColaborador(codigo);
		boolean ret = false;

		if (pojo == null) {

			pojo = new Colaborador();
			pojo.setPnombre(primerNombre);
			pojo.setSnombre(segundoNombre);
			pojo.setPapellido(primerApellido);
			pojo.setSapellido(segundoApellido);
			pojo.setCui(cui);

			pojo.setUnidadEjecutora(UnidadEjecutoraDAO.getUnidadEjecutora(codigoUnidadEjecutora));
			pojo.setUsuario(UsuarioDAO.getUsuario(usuario));

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("3", ColaboradorDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean actualizar(Integer codigo, String primerNombre, String segundoNombre, String otrosNombres,
			String primerApellido, String segundoApellido, String otrosApellidos, Long cui,
			Integer codigoUnidadEjecutora, String usuario) {

		Colaborador pojo = getColaborador(codigo);
		boolean ret = false;

		if (pojo != null) {
			pojo.setPnombre(primerNombre);
			pojo.setSnombre(segundoNombre);
			pojo.setPapellido(primerApellido);
			pojo.setSapellido(segundoApellido);
			pojo.setCui(cui);

			pojo.setUnidadEjecutora(UnidadEjecutoraDAO.getUnidadEjecutora(codigoUnidadEjecutora));
			pojo.setUsuario(UsuarioDAO.getUsuario(usuario));

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("4", ColaboradorDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static List<Colaborador> getPagina(int pagina, int registros) {
		List<Colaborador> ret = new ArrayList<Colaborador>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Colaborador> criteria = session.createQuery("SELECT e FROM Colaborador e", Colaborador.class);
			criteria.setFirstResult(((pagina - 1) * (registros)));
			criteria.setMaxResults(registros);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("5", ColaboradorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros) {
		String jsonEntidades = "";

		List<Colaborador> pojos = getPagina(pagina, registros);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (Colaborador pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.primerNombre = pojo.getPnombre();
			estructuraPojo.segundoNombre = pojo.getSnombre();
			estructuraPojo.primerApellido = pojo.getPapellido();
			estructuraPojo.segundoApellido = pojo.getSapellido();
			estructuraPojo.cui = pojo.getCui();

			estructuraPojo.usuario = pojo.getUsuario().getUsuario();
			estructuraPojo.unidadEjecutora = pojo.getUnidadEjecutora().getUnidadEjecutora();
			estructuraPojo.nombreUnidadEjecutora = pojo.getUnidadEjecutora().getNombre();

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("unidadesEjecutoras", listaEstructuraPojos);

		return jsonEntidades;
	}
	public static String getJson2() {
		String jsonEntidades = "";

		List<Colaborador> pojos = getPagina(1, 10000);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (Colaborador pojo : pojos) {
			if(pojo.getUsuario()==null){
				EstructuraPojo estructuraPojo = new EstructuraPojo();
				estructuraPojo.id = pojo.getId();
				estructuraPojo.primerNombre = pojo.getPnombre();
				estructuraPojo.segundoNombre = pojo.getSnombre();
				estructuraPojo.primerApellido = pojo.getPapellido();
				estructuraPojo.segundoApellido = pojo.getSapellido();
				estructuraPojo.cui = pojo.getCui();
				estructuraPojo.usuario = pojo.getUsuario().getUsuario();
				estructuraPojo.unidadEjecutora = pojo.getUnidadEjecutora().getUnidadEjecutora();
				estructuraPojo.nombreUnidadEjecutora = pojo.getUnidadEjecutora().getNombre();
				listaEstructuraPojos.add(estructuraPojo);
			}
			
		}

		jsonEntidades = Utils.getJSonString("unidadesEjecutoras", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static Long getTotal() {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Long> conteo = session.createQuery("SELECT count(e.id) FROM Colaborador e", Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", ColaboradorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean validarUsuario(String usuario){
			return UsuarioDAO.getUsuario(usuario) != null;
	}

}
