package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProductoPropiedad;
import pojo.ProductoPropiedadValor;
import utilities.CFormaDinamica;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class ProductoPropiedadDAO {
	
	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
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

	public static Integer guardar(Integer codigo, String nombre, String descripcion, String usuario, Integer tipo) {

		ProductoPropiedad pojo = getProductoPropiedad(codigo);
		Integer ret = null;

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
				ret = pojo.getId();
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

	public static List<ProductoPropiedad> getPagina(int pagina, int registros, String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion) {
		List<ProductoPropiedad> ret = new ArrayList<ProductoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			String query = "SELECT e FROM ProductoPropiedad e where e.estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " e.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " e.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(e.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<ProductoPropiedad> criteria = session.createQuery(query, ProductoPropiedad.class);
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
	
	public static Long getTotal(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion) {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
				String query = "SELECT count(e.id) FROM ProductoPropiedad e  where e.estado = 1";
				String query_a="";
				if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
					query_a = String.join("",query_a, " e.nombre LIKE '%",filtro_nombre,"%' ");
				if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
					query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " e.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
				if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
					query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(e.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
				query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));				
			Query<Long> conteo = session.createQuery(query, Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("6", ProductoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static String getJsonPorTipo(int idTipoProducto,int idProducto) {
		List<ProductoPropiedad> productoPropiedades = getProdcutoPropiedadesPorTipo(idTipoProducto);
		List<HashMap<String,Object>> campos = new ArrayList<>();
		for(ProductoPropiedad productoPropiedad:productoPropiedades){
			HashMap <String,Object> campo = new HashMap<String, Object>();
			campo.put("id", productoPropiedad.getId());
			campo.put("nombre", productoPropiedad.getNombre());
			campo.put("tipo", productoPropiedad.getDatoTipo().getId());
			ProductoPropiedadValor proyectoPropiedadValor = ProductoPropiedadValorDAO.getValorPorProdcutoYPropiedad(productoPropiedad.getId(), idProducto);
			if (proyectoPropiedadValor !=null ){
				switch ((Integer) campo.get("tipo")){
					case 1:
						campo.put("valor", proyectoPropiedadValor.getValorString());
						break;
					case 2:
						campo.put("valor", proyectoPropiedadValor.getValorEntero());
						break;
					case 3:
						campo.put("valor", proyectoPropiedadValor.getValorDecimal());
						break;
					case 5:
						campo.put("valor", Utils.formatDate(proyectoPropiedadValor.getValorTiempo()));
						break;
				}
			}
			else{
				campo.put("valor", "");
			}
			campos.add(campo);
		}
		
		String response_text = CFormaDinamica.convertirEstructura(campos);
        response_text = String.join("", "\"productopropiedades\":",response_text);
        response_text = String.join("", "{\"success\":true,", response_text,"}");

		return response_text;
	}
	
	public static List<ProductoPropiedad> getProdcutoPropiedadesPorTipo(int idTipoPropiedad) {
		List<ProductoPropiedad> ret = new ArrayList<ProductoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProductoPropiedad> criteria = session.createQuery("select p from ProductoPropiedad p "
					+ "inner join p.prodtipoPropiedads ptp "
					+ "inner join ptp.productoTipo pt "
					+ "where pt.id = ?1 "
					+ "and p.estado = 1",ProductoPropiedad.class);
			criteria.setParameter(1, idTipoPropiedad);
			ret = criteria.getResultList();
			
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("7", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static ProductoPropiedad getProductoPropiedadPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProductoPropiedad ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProductoPropiedad> criteria = builder.createQuery(ProductoPropiedad.class);
			Root<ProductoPropiedad> root = criteria.from(ProductoPropiedad.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal( root.get("estado"), 1 )));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("8", ProductoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
