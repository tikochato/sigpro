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

import pojo.SubproductoPropiedad;
import pojo.SubproductoPropiedadValor;
import utilities.CFormaDinamica;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class SubproductoPropiedadDAO {
	static class EstructuraPojo {
		Integer id;
		String nombre;
		String descripcion;
		Integer idTipo;
		String tipo;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
	}
	
	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
	}
       

	public static SubproductoPropiedad getSubproductoPropiedad(Integer codigo) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		SubproductoPropiedad ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<SubproductoPropiedad> criteria = builder.createQuery(SubproductoPropiedad.class);
			Root<SubproductoPropiedad> root = criteria.from(SubproductoPropiedad.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), codigo));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("1", SubproductoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static Integer guardar(Integer codigo, String nombre, String descripcion, String usuario, Integer tipo) {

		SubproductoPropiedad pojo = getSubproductoPropiedad(codigo);
		Integer ret = null;

		if (pojo == null) {
			pojo = new SubproductoPropiedad();
			pojo.setNombre(nombre);
			pojo.setDescripcion(descripcion);
			pojo.setEstado(1);
			pojo.setUsuarioCreo(usuario);
			pojo.setFechaCreacion(new Date());
			
			pojo.setSubprodtipoPropiedads(null);
			pojo.setSubproductoPropiedadValors(null);
			
			pojo.setDatoTipo(DatoTipoDAO.getDatoTipo(tipo));

			Session session = CHibernateSession.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(pojo);
				session.getTransaction().commit();
				ret = pojo.getId();
			} catch (Throwable e) {
				CLogger.write("2", SubproductoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean actualizar(Integer codigo, String nombre, String descripcion, String usuario, Integer tipo) {

		SubproductoPropiedad pojo = getSubproductoPropiedad(codigo);
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
				CLogger.write("3", SubproductoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static boolean eliminar(Integer codigo, String usuario) {

		SubproductoPropiedad pojo = getSubproductoPropiedad(codigo);
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
				CLogger.write("4", SubproductoPropiedadDAO.class, e);
			} finally {
				session.close();
			}
		}

		return ret;
	}

	public static List<SubproductoPropiedad> getPagina(int pagina, int registros, String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion) {
		List<SubproductoPropiedad> ret = new ArrayList<SubproductoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			String query = "SELECT e FROM SubproductoPropiedad e where e.estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " e.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " e.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(e.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<SubproductoPropiedad> criteria = session.createQuery(query, SubproductoPropiedad.class);
			criteria.setFirstResult(((pagina - 1) * (registros)));
			criteria.setMaxResults(registros);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("5", SubproductoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros ,String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion) {
		String jsonEntidades = "";

		List<SubproductoPropiedad> pojos = getPagina(pagina, registros, filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();

		for (SubproductoPropiedad pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getNombre();
			estructuraPojo.descripcion = pojo.getDescripcion();
			estructuraPojo.idTipo = pojo.getDatoTipo().getId();
			estructuraPojo.tipo = pojo.getDatoTipo().getNombre();
			estructuraPojo.usuarioCreo = pojo.getUsuarioCreo();
			estructuraPojo.usuarioActualizo = pojo.getUsuarioActualizo();
			estructuraPojo.fechaCreacion = Utils.formatDateHour(pojo.getFechaCreacion());
			estructuraPojo.fechaActualizacion =Utils.formatDateHour(pojo.getFechaActualizacion());

			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("subproductoPropiedades", listaEstructuraPojos);

		return jsonEntidades;
	}

	public static Long getTotal(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion) {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
				String query = "SELECT count(e.id) FROM SubproductoPropiedad e  where e.estado = 1";
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
			CLogger.write("6", SubproductoPropiedadDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static String getJsonPorTipo(int idTipoProducto,int idSubproducto) {
		List<SubproductoPropiedad> subproductoPropiedades = getSubprodcutoPropiedadesPorTipo(idTipoProducto);
		List<HashMap<String,Object>> campos = new ArrayList<>();
		for(SubproductoPropiedad subproductoPropiedad: subproductoPropiedades){
			HashMap <String,Object> campo = new HashMap<String, Object>();
			campo.put("id", subproductoPropiedad.getId());
			campo.put("nombre", subproductoPropiedad.getNombre());
			campo.put("tipo", subproductoPropiedad.getDatoTipo().getId());
			SubproductoPropiedadValor proyectoPropiedadValor = SubproductoPropiedadValorDAO.getValorPorSubProdcutoYPropiedad(subproductoPropiedad.getId(), idSubproducto);
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
        response_text = String.join("", "\"subproductopropiedades\":",response_text);
        response_text = String.join("", "{\"success\":true,", response_text,"}");

		return response_text;
	}
	
	public static List<SubproductoPropiedad> getSubprodcutoPropiedadesPorTipo(int idTipoPropiedad) {
		List<SubproductoPropiedad> ret = new ArrayList<SubproductoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<SubproductoPropiedad> criteria = session.createQuery("select p from SubproductoPropiedad p "
					+ "inner join p.subprodtipoPropiedads ptp "
					+ "inner join ptp.subproductoTipo pt "
					+ "where pt.id = ?1 "
					+ "and p.estado = 1",SubproductoPropiedad.class);
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
	
	public static SubproductoPropiedad getSubproductoPropiedadPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		SubproductoPropiedad ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<SubproductoPropiedad> criteria = builder.createQuery(SubproductoPropiedad.class);
			Root<SubproductoPropiedad> root = criteria.from(SubproductoPropiedad.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal( root.get("estado"), 1 )));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("8", SubproductoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
