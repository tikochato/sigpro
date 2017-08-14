package dao;

import java.util.ArrayList;
import java.util.Date;
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
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		String nombreCompleto;
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
			Integer codigoUnidadEjecutora, String usuario, String usuario_creacion, Date fecha_creacion) {

		Colaborador pojo = getColaborador(codigo);
		boolean ret = false;

		if (pojo == null) {

			pojo = new Colaborador(UnidadEjecutoraDAO.getUnidadEjecutora(codigoUnidadEjecutora),UsuarioDAO.getUsuario(usuario),
					primerNombre, segundoNombre, primerApellido, segundoApellido, cui, 1, usuario_creacion, null, fecha_creacion, null,
					null, null, null,null);
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

	public static boolean actualizar(Integer codigo, String primerNombre, String segundoNombre, 
			String primerApellido, String segundoApellido, Long cui,
			Integer codigoUnidadEjecutora, String usuario, String usuarioc) {

		Colaborador pojo = getColaborador(codigo);
		boolean ret = false;

		if (pojo != null) {
			pojo.setPnombre(primerNombre);
			pojo.setSnombre(segundoNombre);
			pojo.setPapellido(primerApellido);
			pojo.setSapellido(segundoApellido);
			pojo.setCui(cui);
			pojo.setUsuarioActualizo(usuarioc);
			pojo.setFechaActualizacion(new Date());

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
	
	public static boolean borrar(Integer id, String usuarioc) {

		Colaborador pojo = getColaborador(id);
		pojo.setEstado(0);
		pojo.setUsuarioActualizo(usuarioc);
		pojo.setFechaActualizacion(new Date());
		boolean ret = false;

		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();
				ret = true;
		} catch (Throwable e) {
			CLogger.write("5", ColaboradorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static List<Colaborador> getPagina(int pagina, int registros,String filtro_pnombre, String filtro_snombre, String filtro_papellido, String filtro_sapellido,
			String filtro_cui, String filtro_unidad_ejecutora, String columna_ordenada, String orden_direccion, String excluir) {
		List<Colaborador> ret = new ArrayList<Colaborador>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			String query = "SELECT c FROM Colaborador c WHERE c.estado=1 ";
			String query_a="";
			if(filtro_pnombre!=null && filtro_pnombre.trim().length()>0)
				query_a = String.join("",query_a, " c.pnombre LIKE '%",filtro_pnombre,"%' ");
			if(filtro_snombre!=null && filtro_snombre.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.snombre LIKE '%", filtro_snombre,"%' ");
			if(filtro_papellido!=null && filtro_papellido.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.papellido LIKE '%", filtro_papellido,"%' ");
			if(filtro_sapellido!=null && filtro_sapellido.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.sapellido LIKE '%", filtro_sapellido,"%' ");
			if(filtro_cui!=null && filtro_cui.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(c.cui) LIKE '%", filtro_cui,"%' ");
			if(filtro_unidad_ejecutora!=null && filtro_unidad_ejecutora.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.unidadEjecutora.nombre LIKE '%", filtro_unidad_ejecutora,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = String.join(" ", query, (excluir!=null && excluir.length()>0 ? "and c.id not in (" + excluir + ")" : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<Colaborador> criteria = session.createQuery(query,Colaborador.class);
			criteria.setFirstResult(((pagina-1)*(registros)));
			criteria.setMaxResults(registros);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("6", ColaboradorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros, String filtro_pnombre, String filtro_snombre, String filtro_papellido, String filtro_sapellido,
			String filtro_cui, String filtro_unidad_ejecutora, String columna_ordenada, String orden_direccion) {
		String jsonEntidades = "";

		List<Colaborador> pojos = getPagina(pagina, registros, filtro_pnombre, filtro_snombre, filtro_papellido, filtro_sapellido,
				filtro_cui, filtro_unidad_ejecutora, columna_ordenada, orden_direccion,null);

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
			
			estructuraPojo.usuarioCreo = pojo.getUsuarioCreo();
			estructuraPojo.usuarioActualizo = pojo.getUsuarioActualizo();
			estructuraPojo.fechaCreacion = Utils.formatDateHour(pojo.getFechaCreacion());
			estructuraPojo.fechaActualizacion = Utils.formatDateHour(pojo.getFechaActualizacion());
			estructuraPojo.nombreCompleto = String.join(" ", estructuraPojo.primerNombre,
					estructuraPojo.segundoNombre!=null ? estructuraPojo.segundoNombre : "" ,
					estructuraPojo.primerApellido !=null ? estructuraPojo.primerApellido : "" ,
					estructuraPojo.segundoApellido !=null ? estructuraPojo.segundoApellido : "");

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("colaboradores", listaEstructuraPojos);

		return jsonEntidades;
	}
	public static String getJson2() {
		String jsonEntidades = "";

		List<Colaborador> pojos = getPagina(1, 10000, null, null, null, null, null, null, null, null,null);

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
				estructuraPojo.usuarioCreo = pojo.getUsuarioCreo();
				estructuraPojo.usuarioActualizo = pojo.getUsuarioActualizo();
				estructuraPojo.fechaCreacion = Utils.formatDateHour(pojo.getFechaCreacion());
				estructuraPojo.fechaActualizacion = Utils.formatDateHour(pojo.getFechaActualizacion());
				estructuraPojo.nombreCompleto = String.join(" ", estructuraPojo.primerNombre,
						estructuraPojo.segundoNombre!=null ? estructuraPojo.segundoNombre : "" ,
						estructuraPojo.primerApellido !=null ? estructuraPojo.primerApellido : "" ,
						estructuraPojo.segundoApellido !=null ? estructuraPojo.segundoApellido : "");
				
				listaEstructuraPojos.add(estructuraPojo);
			}
			
		}

		jsonEntidades = Utils.getJSonString("colaboradores", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static Long getTotal(String filtro_pnombre, String filtro_snombre, String filtro_papellido, String filtro_sapellido, String filtro_cui, String filtro_unidad_ejecutora) {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			String query = "SELECT count(c.id) FROM Colaborador c WHERE c.estado=1 ";
			String query_a="";
			if(filtro_pnombre!=null && filtro_pnombre.trim().length()>0)
				query_a = String.join("",query_a, " c.pnombre LIKE '%",filtro_pnombre,"%' ");
			if(filtro_snombre!=null && filtro_snombre.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.snombre LIKE '%", filtro_snombre,"%' ");
			if(filtro_papellido!=null && filtro_papellido.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.papellido LIKE '%", filtro_papellido,"%' ");
			if(filtro_sapellido!=null && filtro_sapellido.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.sapellido LIKE '%", filtro_sapellido,"%' ");
			if(filtro_cui!=null && filtro_cui.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(c.cui) LIKE '%", filtro_cui,"%' ");
			if(filtro_unidad_ejecutora!=null && filtro_unidad_ejecutora.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.unidadEjecutora.nombre LIKE '%", filtro_unidad_ejecutora,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> criteria = session.createQuery(query,Long.class);
			ret = criteria.getSingleResult();
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
