package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Producto;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class ProductoDAO {

	
	static class EstructuraPojo {
		Integer id;
		String nombre;
		String descripcion;
		Integer idComponente;
		String componente;
		Integer idProducto;
		String producto;
		Integer idProductoTipo;
		String productoTipo;
		Integer unidadEjectuora;
		String nombreUnidadEjecutora;
		Long snip;
		Integer programa;
		Integer subprograma;
		Integer proyecto_;
		Integer actividad;
		Integer obra;
		Integer fuente;
		Integer estado;
		String fechaCreacion;
		String usuarioCreo;
		String fechaactualizacion;
		String usuarioactualizo;
		
	}

	public static List<Producto> getProductos(String usuario) {
		List<Producto> ret = new ArrayList<Producto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Producto> criteria = session.createQuery("FROM Producto p where p.id in (SELECT u.id.productoid from ProductoUsuario u where u.id.usuario=:usuario )", Producto.class);
			criteria.setParameter("usuario", usuario);
			ret =   (List<Producto>)criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("1", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static Producto getProductoPorId(int id, String usuario) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Producto ret = null;
		try {
			Query<Producto> criteria = session.createQuery("FROM Producto where id=:id AND id in (SELECT u.id.productoid from ProductoUsuario u where u.id.usuario=:usuario )", Producto.class);
			criteria.setParameter("id", id);
			criteria.setParameter("usuario", usuario);
			 ret = (Producto) criteria.getSingleResult();;
		} catch (Throwable e) {
			CLogger.write("2", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardarProducto(Producto Producto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(Producto);
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("3", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean eliminarProducto(Producto Producto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Producto.setEstado(0);
			session.beginTransaction();
			session.update(Producto);
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("4", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean eliminarTotalProducto(Producto Producto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			session.delete(Producto);
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("5", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static List<Producto> getProductosPagina(int pagina, int numeroProductos,Integer componenteid, 
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion, String columna_ordenada, 
			String orden_direccion,String usuario) {
		List<Producto> ret = new ArrayList<Producto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			
			String query = "SELECT p FROM Producto p WHERE p.estado = 1 "
					+ (componenteid!=null && componenteid > 0 ? "AND p.componente.id = :idComp " : "");
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario )");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<Producto> criteria = session.createQuery(query,Producto.class);
			criteria.setParameter("usuario", usuario);
			if (componenteid!=null && componenteid>0){
				criteria.setParameter("idComp", componenteid);
			}
			criteria.setFirstResult(((pagina - 1) * (numeroProductos)));
			criteria.setMaxResults(numeroProductos);
			
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("6", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static Long getTotalProductos(Integer componenteid, String usuario) {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM Producto p WHERE p.estado = 1  "
					+ (componenteid!=null && componenteid > 0 ? "AND p.componente.id = :idComp " : "") + " AND p.id in (SELECT u.id.productoid from ProductoUsuario u where u.id.usuario=:usuario )",
					Long.class);
			conteo.setParameter("usuario", usuario);
			if (componenteid!=null && componenteid > 0){
				conteo.setParameter("idComp", componenteid);
			}
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros,Integer componenteid, String usuario
			,String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion) {
		String jsonEntidades = "";

		List<Producto> pojos = getProductosPagina(pagina, registros,componenteid
				,filtro_nombre, filtro_usuario_creo,filtro_fecha_creacion
				,columna_ordenada,orden_direccion,usuario);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (Producto pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getNombre();
			estructuraPojo.descripcion = pojo.getDescripcion();
			estructuraPojo.programa = pojo.getPrograma();
			estructuraPojo.subprograma = pojo.getSubprograma();
			estructuraPojo.proyecto_ = pojo.getProyecto();
			estructuraPojo.obra = pojo.getObra();
			estructuraPojo.actividad = pojo.getActividad();
			estructuraPojo.fuente = pojo.getFuente();
			estructuraPojo.snip = pojo.getSnip();
			estructuraPojo.estado = pojo.getEstado();
			estructuraPojo.usuarioCreo = pojo.getUsuarioCreo();
			estructuraPojo.usuarioactualizo = pojo.getUsuarioActualizo();
			estructuraPojo.fechaCreacion = Utils.formatDate(pojo.getFechaCreacion());
			estructuraPojo.fechaactualizacion = Utils.formatDate(pojo.getFechaActualizacion());
			

			if (pojo.getComponente() != null) {
				estructuraPojo.idComponente = pojo.getComponente().getId();
				estructuraPojo.componente = pojo.getComponente().getNombre();
			}

			if (pojo.getProductoTipo() != null) {
				estructuraPojo.idProductoTipo = pojo.getProductoTipo().getId();
				estructuraPojo.productoTipo = pojo.getProductoTipo().getNombre();
			}
			if (pojo.getProductoid()>0){
				Producto producto_temp = getProductoPorId(pojo.getProductoid());
				estructuraPojo.idProducto = producto_temp.getId();
				estructuraPojo.producto = producto_temp.getNombre();
			}
			if (pojo.getUnidadEjecutora() != null){
				estructuraPojo.unidadEjectuora = pojo.getUnidadEjecutora().getUnidadEjecutora();
				estructuraPojo.nombreUnidadEjecutora = pojo.getUnidadEjecutora().getNombre();
			}

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("productos", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static boolean guardar(String nombre, String descripcion, Integer componente, Integer productoPadre,
			Integer tipo, String propiedades, String actividades, String usuario) {
		boolean ret = false;

		Producto pojo = new Producto();

		pojo.setNombre(nombre);
		pojo.setDescripcion(descripcion);

		pojo.setComponente(componente > 0 ? ComponenteDAO.getComponentePorId(componente, usuario) : null);
		pojo.setProductoTipo(tipo > 0 ? ProductoTipoDAO.getProductoTipo(tipo) : null);

		pojo.setProductoPropiedadValors(null);
		
		pojo.setUsuarioCreo(usuario);
		pojo.setFechaCreacion(new Date());

		pojo.setEstado(1);

		Session session = CHibernateSession.getSessionFactory().openSession();
		Integer productoid = null;
		try {
			session.beginTransaction();
			productoid = (Integer) session.save(pojo);
			session.getTransaction().commit();

			ret = ProductoPropiedadValorDAO.persistirValores(productoid, propiedades, usuario);

		} catch (Throwable e) {
			CLogger.write("8", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean actualizar(Integer productoId, String nombre, String descripcion, Integer componente,
			Integer productoPadre, Integer tipo, String propiedades, String actividades, String usuario) {
		boolean ret = false;

		Producto pojo = getProductoPorId(productoId,usuario);

		if (pojo != null) {
			pojo.setNombre(nombre);
			pojo.setDescripcion(descripcion);

			pojo.setComponente(componente != null ? ComponenteDAO.getComponentePorId(componente,usuario) : null);
			pojo.setProductoTipo(tipo != null ? ProductoTipoDAO.getProductoTipo(tipo) : null);

			pojo.setUsuarioActualizo(usuario);
			pojo.setFechaActualizacion(new Date());

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();

				ret = ProductoPropiedadValorDAO.persistirValores(productoId, propiedades, usuario);

			} catch (Throwable e) {
				CLogger.write("9", ProductoDAO.class, e);
			} finally {
				session.close();
			}
		}
		return ret;
	}

	public static boolean eliminar(Integer productoId, String usuario) {
		boolean ret = false;

		Producto pojo = getProductoPorId(productoId,usuario);

		if (pojo != null) {
			pojo.setEstado(0);

			Session session = CHibernateSession.getSessionFactory().openSession();

			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();

				ret = true;

			} catch (Throwable e) {
				CLogger.write("10", ProductoDAO.class, e);
			} finally {
				session.close();
			}
		}
		return ret;
	}
	
	public static Producto getProductoPorId(int id) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Producto ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Producto> criteria = builder.createQuery(Producto.class);
			Root<Producto> root = criteria.from(Producto.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ProductoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
