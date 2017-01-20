package dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.lang.reflect.Type;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import pojo.Producto;
import pojo.ProductoPropiedad;
import pojo.ProductoPropiedadValor;
import pojo.ProductoPropiedadValorId;
import pojo.ProyectoPropedadValor;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class ProductoPropiedadValorDAO {


	static class EstructuraPojo {
		Integer propiedadid;
		Integer productoid;

		Integer valorEntero;
		String valorString;
		BigDecimal valorDecimal;
		Date valorTiempo;

		String estado;
	}
	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
	}

	public static ProductoPropiedadValor getProductoPropiedadValor(Integer propiedadId, Integer productoId) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProductoPropiedadValor ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProductoPropiedadValor> criteria = builder.createQuery(ProductoPropiedadValor.class);
			Root<ProductoPropiedadValor> root = criteria.from(ProductoPropiedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new ProductoPropiedadValorId(propiedadId, productoId)));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("1", ProductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardar(Integer propiedadId, Integer productoId, BigDecimal valorDecimal, Integer valorEntero,
			String valorTexto, Date valorTiempo, String usuario) {

		ProductoPropiedadValor pojo = getProductoPropiedadValor(propiedadId, propiedadId);
		boolean ret = false;

		if (pojo == null) {
			pojo = new ProductoPropiedadValor();

			pojo.setId(new ProductoPropiedadValorId(propiedadId, productoId));
			pojo.setProducto(ProductoDAO.getProductoPorId(productoId));
			pojo.setProductoPropiedad(ProductoPropiedadDAO.getProductoPropiedad(propiedadId));

			pojo.setValorDecimal(valorDecimal);
			pojo.setValorEntero(valorEntero);
			pojo.setValorString(valorTexto);
			pojo.setValorTiempo(valorTiempo);

			pojo.setUsuarioCreo(usuario);
			pojo.setFechaCreacion(new Date());
			pojo.setEstado(1);

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("2", ProductoPropiedadValorDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean actualizar(Integer propiedadId, Integer productoId, BigDecimal valorDecimal,
			Integer valorEntero, String valorTexto, Date valorTiempo, String usuario) {

		ProductoPropiedadValor pojo = getProductoPropiedadValor(propiedadId, productoId);
		boolean ret = false;

		if (pojo != null) {
			pojo.setValorDecimal(valorDecimal);
			pojo.setValorEntero(valorEntero);
			pojo.setValorString(valorTexto);
			pojo.setValorTiempo(valorTiempo);

			pojo.setUsuarioActualizo(usuario);
			pojo.setFechaActualizacion(new Date());

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("3", ProductoPropiedadValorDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean eliminar(Integer propiedadId, Integer productoId, String usuario) {

		ProductoPropiedadValor pojo = getProductoPropiedadValor(propiedadId, productoId);
		boolean ret = false;

		if (pojo != null) {

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.delete(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("4", ProductoPropiedadValorDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static List<ProductoPropiedadValor> getPagina(int pagina, int registros, Integer productoId) {
		List<ProductoPropiedadValor> ret = new ArrayList<ProductoPropiedadValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<ProductoPropiedadValor> criteria = session.createQuery(
					"SELECT e FROM ProductoPropiedadValor e where e.id.productoId = " + productoId,
					ProductoPropiedadValor.class);
			criteria.setFirstResult(((pagina - 1) * (registros)));
			criteria.setMaxResults(registros);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("5", ProductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros, Integer productoId) {
		String jsonEntidades = "";

		List<ProductoPropiedadValor> pojos = getPagina(pagina, registros, productoId);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (ProductoPropiedadValor pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();

			estructuraPojo.productoid = pojo.getProducto().getId();
			estructuraPojo.propiedadid = pojo.getProductoPropiedad().getId();

			estructuraPojo.valorEntero = pojo.getValorEntero();
			estructuraPojo.valorString = pojo.getValorString();
			estructuraPojo.valorDecimal = pojo.getValorDecimal();
			estructuraPojo.valorTiempo = pojo.getValorTiempo();

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
			Query<Long> conteo = session.createQuery("SELECT count(e) FROM ProductoPropiedadValor e", Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", ProductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static List<ProductoPropiedadValor> getProductoPropiedadValor(Integer productoId) {
		List<ProductoPropiedadValor> ret = new ArrayList<ProductoPropiedadValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<ProductoPropiedadValor> criteria = session.createQuery(
					"SELECT e FROM ProductoPropiedadValor e where e.id.productoid = :id", ProductoPropiedadValor.class);
			criteria.setParameter("id", productoId);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("8", ProductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(Integer productoId) {
		String jsonEntidades = "";

		List<ProductoPropiedadValor> pojos = getProductoPropiedadValor(productoId);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (ProductoPropiedadValor pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();

			estructuraPojo.productoid = pojo.getProducto().getId();
			estructuraPojo.propiedadid = pojo.getProductoPropiedad().getId();

			estructuraPojo.valorEntero = pojo.getValorEntero();
			estructuraPojo.valorString = pojo.getValorString();
			estructuraPojo.valorDecimal = pojo.getValorDecimal();
			estructuraPojo.valorTiempo = pojo.getValorTiempo();
			estructuraPojo.estado = "C";

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("productoTipos", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static boolean persistirValores(Integer productoid, String propiedades, String usuario) {
		boolean ret = true;
		try{
			Gson gson = new Gson();
			Type type = new TypeToken<List<stdatadinamico>>() {
			}.getType();
			List<stdatadinamico> datos = gson.fromJson(propiedades, type);
			Producto producto = new Producto();
			producto.setId(productoid);
			
			for (stdatadinamico data : datos) {
				ProductoPropiedad productoPropiedad = ProductoPropiedadDAO.getProductoPropiedad(Integer.parseInt(data.id));
				ProductoPropiedadValorId idValor = new ProductoPropiedadValorId(Integer.parseInt(data.id),productoid);
				ProductoPropiedadValor valor = new ProductoPropiedadValor(idValor, producto, productoPropiedad, usuario, new DateTime().toDate());

				switch (productoPropiedad.getDatoTipo().getId()){
					case 1:
						valor.setValorString(data.valor);
						break;
					case 2:
						valor.setValorEntero(Integer.parseInt(data.valor));
						break;
					case 3:
						valor.setValorDecimal(new BigDecimal(data.valor));
						break;
					case 4:

						break;
					case 5:
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						valor.setValorTiempo(sdf.parse(data.valor));
						break;
				}
				ret = (ret && ProductoPropiedadValorDAO.guardarProductoPropiedadValor(valor));
			}
			
		}
		catch (Exception e){
			return false;
		}
		return ret;
	}
	
	public static ProductoPropiedadValor getValorPorProdcutoYPropiedad(int idPropiedad,int idProducto){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProductoPropiedadValor ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ProductoPropiedadValor> criteria = builder.createQuery(ProductoPropiedadValor.class);
			Root<ProductoPropiedadValor> root = criteria.from(ProductoPropiedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new ProductoPropiedadValorId( idPropiedad,idProducto)));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("9", ProductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarProductoPropiedadValor(ProductoPropiedadValor productoPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(productoPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("10", ProductoPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	
	

}
