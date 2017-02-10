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
	static class EstructuraPojo {
		Integer id;
		String nombre;
		String descripcion;
		Integer idTipo;
		String tipo;
	}
	
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

	public static boolean guardar(Integer codigo, String nombre, String descripcion, String usuario, Integer tipo) {

		ProductoPropiedad pojo = getProductoPropiedad(codigo);
		boolean ret = false;

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
				ret = true;
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

	public static List<ProductoPropiedad> getPagina(int pagina, int registros) {
		List<ProductoPropiedad> ret = new ArrayList<ProductoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<ProductoPropiedad> criteria = session.createQuery("SELECT e FROM ProductoPropiedad e where e.estado = 1", ProductoPropiedad.class);
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

	public static String getJson(int pagina, int registros) {
		String jsonEntidades = "";

		List<ProductoPropiedad> pojos = getPagina(pagina, registros);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (ProductoPropiedad pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getNombre();
			estructuraPojo.descripcion = pojo.getDescripcion();
			estructuraPojo.idTipo = pojo.getDatoTipo().getId();
			estructuraPojo.tipo = pojo.getDatoTipo().getNombre();

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("productoPropiedades", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static Long getTotal() {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Long> conteo = session.createQuery("SELECT count(e.id) FROM ProductoPropiedad e  where e.estado = 1", Long.class);
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
