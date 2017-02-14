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
		String usuarioCreo;
		String usuairoActulizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
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

	public static boolean guardar(Integer codigo, String nombre, String descripcion, String propiedades,
			String usuario) {

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
				Integer id = (Integer) session.save(pojo);
				session.getTransaction().commit();

				ret = ProdTipoPropiedadDAO.persistirPropiedades(id, propiedades, usuario);

			} catch (Throwable e) {
				CLogger.write("2", ProductoTipoDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean actualizar(Integer codigo, String nombre, String descripcion, String propiedades,
			String usuario) {

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

				ret = ProdTipoPropiedadDAO.persistirPropiedades(codigo, propiedades, usuario);
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

	public static List<ProductoTipo> getPagina(int pagina, int registros, String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion) {
		List<ProductoTipo> ret = new ArrayList<ProductoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			String query = "SELECT p FROM ProductoTipo p where p.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<ProductoTipo> criteria = session.createQuery(query,ProductoTipo.class);
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

	public static String getJson(int pagina, int registros, String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion) {
		String jsonEntidades = "";

		List<ProductoTipo> pojos = getPagina(pagina, registros,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (ProductoTipo pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getNombre();
			estructuraPojo.descripcion = pojo.getDescripcion();
			estructuraPojo.usuarioCreo = pojo.getUsuarioCreo();
			estructuraPojo.usuairoActulizo = pojo.getUsuarioActualizo();
			estructuraPojo.fechaCreacion = Utils.formatDateHour(pojo.getFechaCreacion());
			estructuraPojo.fechaActualizacion = Utils.formatDateHour(pojo.getFechaActualizacion());
			estructuraPojo.estado = pojo.getEstado();

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("productoTipos", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static Long getTotal(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion) {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			String query = "SELECT count(e.id) FROM ProductoTipo e  where e.estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " e.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " e.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(e.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", ProductoTipoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

}
