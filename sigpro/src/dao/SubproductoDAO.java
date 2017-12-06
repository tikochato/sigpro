package dao;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;

import pojo.Actividad;
import pojo.PlanAdquisicion;
import pojo.PlanAdquisicionPago;
import pojo.Proyecto;
import pojo.Subproducto;
import pojo.SubproductoUsuario;
import pojo.SubproductoUsuarioId;
import pojo.Usuario;
import utilities.CHibernateSession;
import utilities.CHistoria;
import utilities.CLogger;
import utilities.Utils;

public class SubproductoDAO {

	
	public static class EstructuraPojo {
		public Integer id;
		public String nombre;
		public String descripcion;
		public Integer idProducto;
		public String producto;
		public Integer idSubproductoTipo;
		public String subProductoTipo;
		public Integer unidadEjecutora;
		public String nombreUnidadEjecutora;
		public String entidadnombre;
		public Integer entidadentidad;
		public Integer ejercicio;
		public Long snip;
		public Integer programa;
		public Integer subprograma;
		public Integer proyecto_;
		public Integer actividad;
		public Integer obra;
		public Integer renglon;
		public Integer ubicacionGeografica;
		public Integer duracion;
		public String duracionDimension;
		public String fechaInicio;
		public String fechaFin;
		public Integer estado;
		public String fechaCreacion;
		public String usuarioCreo;
		public String fechaActualizacion;
		public String usuarioActualizo;
		public String latitud;
		public String longitud;
		public BigDecimal costo;
		public Integer acumulacionCosto;
		public String acumulacionCostoNombre;
		public boolean tieneHijos;
		public String fechaInicioReal;
		public String fechaFinReal;
		public Integer congelado;
		public String fechaElegibilidad;
		public String fechaCierre;
	}

	public static List<Subproducto> getSubproductos(String usuario) {
		List<Subproducto> ret = new ArrayList<Subproducto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Subproducto> criteria = session.createQuery("FROM Subproducto p where p.id in (SELECT u.id.subproductoid from SubproductoUsuario u where u.id.usuario=:usuario )", Subproducto.class);
			criteria.setParameter("usuario", usuario);
			ret =   (List<Subproducto>)criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("1", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static Subproducto getSubproductoPorId(int id, String usuario) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Subproducto ret = null;
		try {
			List<Subproducto> listRet = null;
			String Str_query = String.join(" ","Select sp FROM Subproducto sp",
					"where id=:id");
			String Str_usuario = "";
			if(usuario != null){
				Str_usuario = String.join(" ", "AND id in (SELECT u.id.subproductoid from SubproductoUsuario u where u.id.usuario=:usuario )");
			}
			
			Str_query = String.join(" ", Str_query, Str_usuario);
			Query<Subproducto> criteria = session.createQuery(Str_query, Subproducto.class);
			criteria.setParameter("id", id);
			if(usuario != null){
				criteria.setParameter("usuario", usuario);
			}
			listRet=criteria.getResultList();
			 ret =!listRet.isEmpty() ? listRet.get(0) : null;
		} catch (Throwable e) {
			CLogger.write("2", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean guardarSubproducto(Subproducto subproducto, boolean calcular_valores_agregados) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			if(subproducto.getId()==null || subproducto.getId()<1){
				session.saveOrUpdate(subproducto);
				session.flush();
				subproducto.setTreePath(subproducto.getProducto().getTreePath()+""+
				(10000000+subproducto.getId()));
			}
			session.saveOrUpdate(subproducto);
			Usuario usu = UsuarioDAO.getUsuario(subproducto.getUsuarioCreo());
			SubproductoUsuario su = new SubproductoUsuario(new SubproductoUsuarioId(subproducto.getId(),subproducto.getUsuarioCreo())
					, subproducto, usu, subproducto.getUsuarioCreo(), subproducto.getFechaCreacion());
			session.saveOrUpdate(su);
			if(!subproducto.getUsuarioCreo().equals("admin")){
				SubproductoUsuario su_admin = new SubproductoUsuario(new SubproductoUsuarioId(subproducto.getId(),"admin")
						, subproducto, usu, subproducto.getUsuarioCreo(), subproducto.getFechaCreacion());
				session.saveOrUpdate(su_admin);
			}
			session.getTransaction().commit();
			session.close();
			
			if(calcular_valores_agregados){
				ProyectoDAO.calcularCostoyFechas(Integer.parseInt(subproducto.getTreePath().substring(0,8))-10000000);
			}
			
			ret = true;
		} catch (Throwable e) {
			CLogger.write("3", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean eliminarSubproducto(Subproducto subproducto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			subproducto.setEstado(0);
			session.beginTransaction();
			session.update(subproducto);
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("4", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static boolean eliminarTotalSubproducto(Subproducto Subproducto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			session.delete(Subproducto);
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("5", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static List<Subproducto> getSubproductosPagina(int pagina, int numeroProductos,Integer productoid, 
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion, String columna_ordenada, 
			String orden_direccion,String usuario) {
		List<Subproducto> ret = new ArrayList<Subproducto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			
			String query = "SELECT p FROM Subproducto p WHERE p.estado = 1 "
					+ (productoid!=null && productoid > 0 ? "AND p.producto.id = :idProducto " : "");
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.subproductoid from SubproductoUsuario u where u.id.usuario=:usuario )");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<Subproducto> criteria = session.createQuery(query,Subproducto.class);
			criteria.setParameter("usuario", usuario);
			if (productoid!=null && productoid>0){
				criteria.setParameter("idProducto", productoid);
			}
			criteria.setFirstResult(((pagina - 1) * (numeroProductos)));
			criteria.setMaxResults(numeroProductos);
			
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("6", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static Long getTotalSubproductos(Integer productoid, String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String usuario) {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			
			String query = "SELECT count(p.id) FROM Subproducto p WHERE p.estado = 1 "
					+ (productoid!=null && productoid > 0 ? "AND p.producto.id = :idProducto " : "");
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.subproductoid from SubproductoUsuario u where u.id.usuario=:usuario )");
			
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("usuario", usuario);
			if (productoid!=null && productoid > 0){
				conteo.setParameter("idProducto", productoid);
			}
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static String getJson(int pagina, int registros,Integer componenteid, String usuario
			,String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion) {
		String jsonEntidades = "";

		List<Subproducto> pojos = getSubproductosPagina(pagina, registros,componenteid
				,filtro_nombre, filtro_usuario_creo,filtro_fecha_creacion
				,columna_ordenada,orden_direccion,usuario);

		List<EstructuraPojo> listaEstructuraPojos = new ArrayList<EstructuraPojo>();
		
		int congelado = 0;
		String fechaElegibilidad = null;
		String fechaCierre = null;
		
		if(pojos!=null && pojos.size()>0){
			Proyecto proyecto = ProyectoDAO.getProyectobyTreePath(pojos.get(0).getTreePath());
			if(proyecto!=null){
				congelado = proyecto.getCongelado()!=null?proyecto.getCongelado():0;
				fechaElegibilidad = Utils.formatDate(proyecto.getFechaElegibilidad());
				fechaCierre = Utils.formatDate(proyecto.getFechaCierre());
			}
		}

		for (Subproducto pojo : pojos) {
			EstructuraPojo estructuraPojo = new EstructuraPojo();
			estructuraPojo.id = pojo.getId();
			estructuraPojo.nombre = pojo.getNombre();
			estructuraPojo.descripcion = pojo.getDescripcion();
			estructuraPojo.programa = pojo.getPrograma();
			estructuraPojo.subprograma = pojo.getSubprograma();
			estructuraPojo.proyecto_ = pojo.getProyecto();
			estructuraPojo.obra = pojo.getObra();
			estructuraPojo.actividad = pojo.getActividad();
			estructuraPojo.renglon = pojo.getRenglon();
			estructuraPojo.ubicacionGeografica = pojo.getUbicacionGeografica();
			estructuraPojo.duracion = pojo.getDuracion();
			estructuraPojo.duracionDimension = pojo.getDuracionDimension();
			estructuraPojo.fechaInicio = Utils.formatDate(pojo.getFechaInicio());
			estructuraPojo.fechaFin = Utils.formatDate(pojo.getFechaFin());
			estructuraPojo.snip = pojo.getSnip();
			estructuraPojo.estado = pojo.getEstado();
			estructuraPojo.usuarioCreo = pojo.getUsuarioCreo();
			estructuraPojo.usuarioActualizo = pojo.getUsuarioActualizo();
			estructuraPojo.fechaCreacion = Utils.formatDateHour(pojo.getFechaCreacion());
			estructuraPojo.fechaActualizacion = Utils.formatDateHour(pojo.getFechaActualizacion());
			estructuraPojo.latitud = pojo.getLatitud();
			estructuraPojo.longitud = pojo.getLongitud();
			estructuraPojo.costo = pojo.getCosto();
			estructuraPojo.acumulacionCosto = pojo.getAcumulacionCosto() != null ? pojo.getAcumulacionCosto().getId() : null;
			estructuraPojo.acumulacionCostoNombre = pojo.getAcumulacionCosto() != null ? pojo.getAcumulacionCosto().getNombre() : null;
			
			if (pojo.getProducto() != null) {
				estructuraPojo.idProducto = pojo.getProducto().getId();
				estructuraPojo.producto = pojo.getProducto().getNombre();
			}

			if (pojo.getSubproductoTipo() != null) {
				estructuraPojo.idSubproductoTipo = pojo.getSubproductoTipo().getId();
				estructuraPojo.subProductoTipo = pojo.getSubproductoTipo().getNombre();
			}
			
			if (pojo.getUnidadEjecutora() != null){
				estructuraPojo.unidadEjecutora = pojo.getUnidadEjecutora().getId().getUnidadEjecutora();
				estructuraPojo.nombreUnidadEjecutora = pojo.getUnidadEjecutora().getNombre();
				estructuraPojo.entidadentidad = pojo.getUnidadEjecutora().getId().getEntidadentidad();
				estructuraPojo.ejercicio = pojo.getUnidadEjecutora().getId().getEjercicio();
				estructuraPojo.entidadnombre = pojo.getUnidadEjecutora().getEntidad().getNombre();
			}
			else if(pojo.getProducto().getUnidadEjecutora()!=null){
				estructuraPojo.unidadEjecutora = pojo.getProducto().getUnidadEjecutora().getId().getUnidadEjecutora();
				estructuraPojo.nombreUnidadEjecutora = pojo.getProducto().getUnidadEjecutora().getNombre();
				estructuraPojo.entidadentidad = pojo.getProducto().getUnidadEjecutora().getId().getEntidadentidad();
				estructuraPojo.ejercicio = pojo.getProducto().getUnidadEjecutora().getId().getEjercicio();
				estructuraPojo.entidadnombre = pojo.getProducto().getUnidadEjecutora().getEntidad().getNombre();
			}
			
			estructuraPojo.tieneHijos = ObjetoDAO.tieneHijos(estructuraPojo.id, 4);
			
			estructuraPojo.fechaInicioReal = Utils.formatDate(pojo.getFechaInicioReal());
			estructuraPojo.fechaFinReal = Utils.formatDate(pojo.getFechaFinReal());
			
			estructuraPojo.congelado = congelado;
			estructuraPojo.fechaElegibilidad = fechaElegibilidad;
			estructuraPojo.fechaCierre = fechaCierre;
			
			listaEstructuraPojos.add(estructuraPojo);
		}

		jsonEntidades = Utils.getJSonString("subproductos", listaEstructuraPojos);

		return jsonEntidades;
	}

		public static boolean eliminar(Integer productoId, String usuario) {
		boolean ret = false;

		Subproducto pojo = getSubproductoPorId(productoId,usuario);

		if (pojo != null) {
			pojo.setEstado(0);
			pojo.setOrden(null);
			Session session = CHibernateSession.getSessionFactory().openSession();

			try {
				session.beginTransaction();
				session.update(pojo);
				session.getTransaction().commit();

				ret = true;

			} catch (Throwable e) {
				CLogger.write("10", SubproductoDAO.class, e);
			} finally {
				session.close();
			}
		}
		return ret;
	}
	
	public static Subproducto getSubproductoPorId(int id) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Subproducto ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();
			List<Subproducto> listRet = null;
			CriteriaQuery<Subproducto> criteria = builder.createQuery(Subproducto.class);
			Root<Subproducto> root = criteria.from(Subproducto.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id )));
			listRet= session.createQuery( criteria ).getResultList();
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("2", SubproductoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public String getSubproductoJson(int id){
		
		Subproducto pojo = getSubproductoPorId(id);
		EstructuraPojo estructuraPojo = new EstructuraPojo();
		estructuraPojo.id = pojo.getId();
		estructuraPojo.nombre = pojo.getNombre();
		
		String subproducto =   Utils.getJSonString("subproducto", estructuraPojo);
		return subproducto;
		
	}
	
	public static Subproducto getSubproductoInicial(Integer productoId, String usuario){
		Subproducto ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "FROM Subproducto sp where sp.estado=1 and sp.orden=1 and sp.producto.id=:productoId and sp.usuarioCreo=:usuario";
			Query<Subproducto> criteria = session.createQuery(query, Subproducto.class);
			criteria.setParameter("productoId", productoId);
			criteria.setParameter("usuario", usuario);
			List<Subproducto> listRet = null;
			listRet = criteria.getResultList();
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}catch(Throwable e){
			CLogger.write("11", SubproductoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Subproducto getSubproductoFechaMaxima(Integer productoId, String usuario){
		Subproducto ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "FROM Subproducto sp where sp.estado=1 and sp.producto.id=:productoId and sp.usuarioCreo=:usuario order by sp.fechaFin desc";
			Query<Subproducto> criteria = session.createQuery(query, Subproducto.class);
			criteria.setMaxResults(1);
			criteria.setParameter("productoId", productoId);
			criteria.setParameter("usuario", usuario);
			List<Subproducto> listRet = null;
			listRet = criteria.getResultList();
			 ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}catch(Throwable e){
			CLogger.write("12", SubproductoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarSubproductoOrden(Subproducto subproducto, Session session) {
		boolean ret = false;
		try {
			session.saveOrUpdate(subproducto);
			session.flush();
			session.clear();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("13", SubproductoDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static BigDecimal calcularCosto(Subproducto subproducto){
		BigDecimal costo = new BigDecimal(0);
		try{
			List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(subproducto.getId(), 4);
			if(actividades != null && actividades.size() > 0){
				for(Actividad actividad : actividades){
					costo = costo.add(actividad.getCosto() != null ? actividad.getCosto() : new BigDecimal(0));
				}
			}else{				
				PlanAdquisicion pa = PlanAdquisicionDAO.getPlanAdquisicionByObjeto(4, subproducto.getId());
				if(pa!=null){
						if(pa.getPlanAdquisicionPagos()!=null && pa.getPlanAdquisicionPagos().size()>0){
							BigDecimal pagos = new BigDecimal(0);
							for(PlanAdquisicionPago pago: pa.getPlanAdquisicionPagos())
								pagos=pagos.add(pago.getPago());
							costo = pagos;
						}
						else
							costo = pa.getTotal();
				}
				else
					costo = subproducto.getCosto();
			}
			
		}catch(Exception e){
			CLogger.write("14", Proyecto.class, e);
		}
		
		return costo;
	}
	
	public static boolean calcularCostoyFechas(Integer subproductoId){
		boolean ret = false;
		ArrayList<ArrayList<Nodo>> listas = EstructuraProyectoDAO.getEstructuraObjetoArbolCalculos(subproductoId, 4);
		for(int i=listas.size()-2; i>=0; i--){
			for(int j=0; j<listas.get(i).size(); j++){
				Nodo nodo = listas.get(i).get(j);
				Double costo=0.0d;
				Timestamp fecha_maxima=new Timestamp(0);
				Timestamp fecha_minima=new Timestamp((new DateTime(2999,12,31,0,0,0)).getMillis());
				for(Nodo nodo_hijo:nodo.children){
					costo += nodo_hijo.costo;
					fecha_minima = (nodo_hijo.fecha_inicio.getTime()<fecha_minima.getTime()) ? nodo_hijo.fecha_inicio : fecha_minima;
					fecha_maxima = (nodo_hijo.fecha_fin.getTime()>fecha_maxima.getTime()) ? nodo_hijo.fecha_fin : fecha_maxima;
				}
				nodo.objeto = ObjetoDAO.getObjetoPorIdyTipo(nodo.id, nodo.objeto_tipo);
				if(nodo.children!=null && nodo.children.size()>0){
					nodo.fecha_inicio = fecha_minima;
					nodo.fecha_fin = fecha_maxima;
					nodo.costo = costo;
				}
				else
					nodo.costo = calcularCosto((Subproducto)nodo.objeto).doubleValue();
				nodo.duracion = Utils.getWorkingDays(new DateTime(nodo.fecha_inicio), new DateTime(nodo.fecha_fin));
				setDatosCalculados(nodo.objeto,nodo.fecha_inicio,nodo.fecha_fin,nodo.costo, nodo.duracion);
			}
			ret = true;
		}
		ret= ret && guardarSubproductoBatch(listas);	
		return ret;
	}
	
	private static void setDatosCalculados(Object objeto,Timestamp fecha_inicio, Timestamp fecha_fin, Double costo, int duracion){
		try{
			if(objeto!=null){
				Method setFechaInicio =objeto.getClass().getMethod("setFechaInicio",Date.class);
				Method setFechaFin =  objeto.getClass().getMethod("setFechaFin",Date.class);
				Method setCosto = objeto.getClass().getMethod("setCosto",BigDecimal.class);
				Method setDuracion = objeto.getClass().getMethod("setDuracion", int.class);
				setFechaInicio.invoke(objeto, new Date(fecha_inicio.getTime()));
				setFechaFin.invoke(objeto, new Date(fecha_fin.getTime()));
				setCosto.invoke(objeto, new BigDecimal(costo));
				setDuracion.invoke(objeto, duracion);
			}
		}
		catch(Throwable e){
			CLogger.write("17", SubproductoDAO.class, e);
		}
		
	}
	
	private static boolean guardarSubproductoBatch(ArrayList<ArrayList<Nodo>> listas){
		boolean ret = true;
		try{
			Session session = CHibernateSession.getSessionFactory().openSession();
			session.beginTransaction();
			int count=0;
			for(int i=0; i<listas.size()-1; i++){
				for(int j=0; j<listas.get(i).size();j++){
					session.saveOrUpdate(listas.get(i).get(j).objeto);
					if ( ++count % 20 == 0 ) {
				        session.flush();
				        session.clear();
				    }
				}
			}
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			ret = false;
			CLogger.write("18", SubproductoDAO.class, e);
		}
		return ret;
	}
	
	public static List<Subproducto> getSubproductosHistory(Integer productoid,String lineaBase) {
		List<Subproducto> ret = new ArrayList<Subproducto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			
			String query = String.join(" ", "select * from sipro_history.subproducto p ",
							"where p.estado = 1",
							"and p.productoid=?1",
							lineaBase != null ? "and p.linea_base like '%" + lineaBase + "%'" : "and p.actual = 1");
 
			
			Query<Subproducto> criteria = session.createNativeQuery(query,Subproducto.class);
			criteria.setParameter(1, productoid);
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("19", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static Subproducto getSubproductoHistory(Integer subproductoId,String lineaBase){
		Subproducto ret = null;
		List<Subproducto> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select * ", 
					"from sipro_history.subproducto p ",
					"where p.estado = 1 ",
					"and p.id = ?1 ",
					lineaBase != null ? "and p.linea_base like '%" + lineaBase + "%'" : "and p.actual = 1",
							"order by p.id desc");
			Query<Subproducto> criteria = session.createNativeQuery(query, Subproducto.class);
			criteria.setParameter(1, subproductoId);
			listRet =   criteria.getResultList();
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("20", SubproductoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static String getVersiones (Integer id){
		String resultado = "";
		String query = "SELECT DISTINCT(version) "
				+ " FROM sipro_history.subproducto "
				+ " WHERE id = "+id;
		List<?> versiones = CHistoria.getVersiones(query);
		if(versiones!=null){
			for(int i=0; i<versiones.size(); i++){
				if(!resultado.isEmpty()){
					resultado+=",";
				}
				resultado+=(Integer)versiones.get(i);
			}
		}
		return resultado;
	}
	
	public static String getHistoria (Integer id, Integer version){
		String resultado = "";
		String query = "SELECT sb.version, sb.nombre, sb.descripcion, st.nombre tipo, ue.nombre unidad_ejecutora, sb.costo, ac.nombre tipo_costo, "
				+ " sb.programa, sb.subprograma, sb.proyecto, sb.actividad, sb.obra, sb.renglon, sb.ubicacion_geografica, sb.latitud, sb.longitud, "
				+ " sb.fecha_inicio, sb.fecha_fin, sb.duracion, sb.fecha_inicio_real, sb.fecha_fin_real, "
				+ " sb.fecha_creacion, sb.usuario_creo, sb.fecha_actualizacion, sb.usuario_actualizo, "
				+ " CASE WHEN sb.estado = 1 "
				+ " THEN 'Activo' "
				+ " ELSE 'Inactivo' "
				+ " END AS estado "
				+ " FROM sipro_history.subproducto sb "
				+ " JOIN sipro.unidad_ejecutora ue ON sb.unidad_ejecutoraunidad_ejecutora = ue.unidad_ejecutora and sb.entidad = ue.entidadentidad and sb.ejercicio = ue.ejercicio   "
				+ " JOIN sipro_history.subproducto_tipo st ON sb.subproducto_tipoid = st.id "
				+ " JOIN sipro_history.acumulacion_costo ac ON sb.acumulacion_costoid = ac.id "
				+ " WHERE sb.id = "+id
				+ " AND sb.version = " +version;
		
		String [] campos = {"Version", "Nombre", "Descripción", "Tipo", "Unidad Ejecutora", "Monto Planificado", "Tipo Acumulación de Monto Planificado", 
				"Programa", "Subprograma", "Proyecto", "Actividad", "Obra", "Renglon", "Ubicación Geográfica", "Latitud", "Longitud", 
				"Fecha Inicio", "Fecha Fin", "Duración", "Fecha Inicio Real", "Fecha Fin Real", 
				"Fecha Creación", "Usuario que creo", "Fecha Actualización", "Usuario que actualizó", 
				"Estado"};
		resultado = CHistoria.getHistoria(query, campos);
		return resultado;
	}
}
