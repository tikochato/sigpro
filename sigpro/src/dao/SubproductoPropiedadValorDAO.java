package dao;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.SubproductoPropiedadValor;
import pojo.SubproductoPropiedadValorId;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class SubproductoPropiedadValorDAO {


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

	public static SubproductoPropiedadValor getSubproductoPropiedadValor(Integer propiedadId, Integer productoId) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		SubproductoPropiedadValor ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<SubproductoPropiedadValor> criteria = builder.createQuery(SubproductoPropiedadValor.class);
			Root<SubproductoPropiedadValor> root = criteria.from(SubproductoPropiedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new SubproductoPropiedadValorId(propiedadId, productoId)));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("1", SubproductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardar(Integer propiedadId, Integer productoId, BigDecimal valorDecimal, Integer valorEntero,
			String valorTexto, Date valorTiempo, String usuario) {

		SubproductoPropiedadValor pojo = getSubproductoPropiedadValor(propiedadId, propiedadId);
		boolean ret = false;

		if (pojo == null) {
			pojo = new SubproductoPropiedadValor();

			pojo.setId(new SubproductoPropiedadValorId(propiedadId, productoId));
			pojo.setSubproducto(SubproductoDAO.getSubproductoPorId(productoId,usuario));
			pojo.setSubproductoPropiedad(SubproductoPropiedadDAO.getSubproductoPropiedad(propiedadId));

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
				CLogger.write("2", SubproductoPropiedadValorDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean actualizar(Integer propiedadId, Integer productoId, BigDecimal valorDecimal,
			Integer valorEntero, String valorTexto, Date valorTiempo, String usuario) {

		SubproductoPropiedadValor pojo = getSubproductoPropiedadValor(propiedadId, productoId);
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
				CLogger.write("3", SubproductoPropiedadValorDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean eliminar(Integer propiedadId, Integer productoId, String usuario) {

		SubproductoPropiedadValor pojo = getSubproductoPropiedadValor(propiedadId, productoId);
		boolean ret = false;

		if (pojo != null) {

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.delete(pojo);
				session.getTransaction().commit();
				ret = true;
			} catch (Throwable e) {
				CLogger.write("4", SubproductoPropiedadValorDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static List<SubproductoPropiedadValor> getPagina(int pagina, int registros, Integer productoId) {
		List<SubproductoPropiedadValor> ret = new ArrayList<SubproductoPropiedadValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<SubproductoPropiedadValor> criteria = session.createQuery(
					"SELECT e FROM SubproductoPropiedadValor e where e.id.productoId = " + productoId,
					SubproductoPropiedadValor.class);
			criteria.setFirstResult(((pagina - 1) * (registros)));
			criteria.setMaxResults(registros);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("5", SubproductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros, Integer productoId) {
		String jsonEntidades = "";

		List<SubproductoPropiedadValor> pojos = getPagina(pagina, registros, productoId);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (SubproductoPropiedadValor pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();

			estructuraPojo.productoid = pojo.getSubproducto().getId();
			estructuraPojo.propiedadid = pojo.getSubproductoPropiedad().getId();

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
			Query<Long> conteo = session.createQuery("SELECT count(e) FROM SubproductoPropiedadValor e", Long.class);
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", SubproductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static List<SubproductoPropiedadValor> getSubproductoPropiedadValor(Integer productoId) {
		List<SubproductoPropiedadValor> ret = new ArrayList<SubproductoPropiedadValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<SubproductoPropiedadValor> criteria = session.createQuery(
					"SELECT e FROM SubproductoPropiedadValor e where e.id.productoid = :id", SubproductoPropiedadValor.class);
			criteria.setParameter("id", productoId);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("8", SubproductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(Integer productoId) {
		String jsonEntidades = "";

		List<SubproductoPropiedadValor> pojos = getSubproductoPropiedadValor(productoId);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (SubproductoPropiedadValor pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();

			estructuraPojo.productoid = pojo.getSubproducto().getId();
			estructuraPojo.propiedadid = pojo.getSubproductoPropiedad().getId();

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

	
	
	public static SubproductoPropiedadValor getValorPorSubProdcutoYPropiedad(int idPropiedad,int idProducto){
		Session session = CHibernateSession.getSessionFactory().openSession();
		SubproductoPropiedadValor ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<SubproductoPropiedadValor> criteria = builder.createQuery(SubproductoPropiedadValor.class);
			Root<SubproductoPropiedadValor> root = criteria.from(SubproductoPropiedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new SubproductoPropiedadValorId( idPropiedad,idProducto)));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("9", SubproductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarSubproductoPropiedadValor(SubproductoPropiedadValor subproductoPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(subproductoPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("10", SubproductoPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	
	

}
