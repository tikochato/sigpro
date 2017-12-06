package dao;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;

import pojo.Actividad;
import pojo.Subcomponente;
import pojo.SubcomponenteUsuario;
import pojo.SubcomponenteUsuarioId;
import pojo.PlanAdquisicion;
import pojo.PlanAdquisicionPago;
import pojo.Producto;
import pojo.Proyecto;
import utilities.CHibernateSession;
import utilities.CHistoria;
import utilities.CLogger;
import utilities.Utils;

public class SubComponenteDAO {
	public static List<Subcomponente> getSubComponentes(String usuario){
		List<Subcomponente> ret = new ArrayList<Subcomponente>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Subcomponente> criteria = session.createQuery("FROM Subcomponente p where estado = 1 AND p.id in (SELECT u.id.subcomponenteid from SubcomponenteUsuario u where u.id.usuario=:usuario )", Subcomponente.class);
			criteria.setParameter("usuario", usuario);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Subcomponente getSubComponentePorId(int id, String usuario){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Subcomponente ret = null;
		List<Subcomponente> listRet = null;
		try{
			String Str_query = String.join(" ", "Select c FROM Subcomponente c",
					"where id=:id");
			String Str_usuario = "";
			if(usuario != null){
				Str_usuario = String.join(" ", "AND id in (SELECT u.id.subcomponenteid from SubcomponenteUsuario u where u.id.usuario=:usuario )");
			}
			
			Str_query = String.join(" ", Str_query, Str_usuario);
			Query<Subcomponente> criteria = session.createQuery(Str_query, Subcomponente.class);
			criteria.setParameter("id", id);
			if(usuario != null){
				criteria.setParameter("usuario", usuario);
			}
			 listRet = criteria.getResultList();
			 
			 ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("2", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarSubComponente(Subcomponente SubComponente, boolean calcular_valores_agregados){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			if(SubComponente.getId()==null || SubComponente.getId()<1){
				session.saveOrUpdate(SubComponente);
				session.flush();
				SubComponente.setTreePath(SubComponente.getComponente().getTreePath()+""+(10000000+SubComponente.getId()));
			}
			session.saveOrUpdate(SubComponente);
			SubcomponenteUsuario cu = new SubcomponenteUsuario(new SubcomponenteUsuarioId(SubComponente.getId(), SubComponente.getUsuarioCreo()), SubComponente);
			session.saveOrUpdate(cu);
			if(!SubComponente.getUsuarioCreo().equals("admin")){
				SubcomponenteUsuario cu_admin = new SubcomponenteUsuario(new SubcomponenteUsuarioId(SubComponente.getId(), "admin"), SubComponente);
				session.saveOrUpdate(cu_admin);
			}
			session.getTransaction().commit();
			session.close();
			
			if(calcular_valores_agregados){
				ProyectoDAO.calcularCostoyFechas(Integer.parseInt(SubComponente.getTreePath().substring(0,8))-10000000);
			}
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarSubComponente(Subcomponente SubComponente){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			SubComponente.setEstado(0);
			SubComponente.setOrden(null);
			session.beginTransaction();
			session.update(SubComponente);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarTotalSubComponente(Subcomponente SubComponente){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(SubComponente);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<Subcomponente> getSubComponentesPagina(int pagina, int numeroSubComponentes, String usuario){
		List<Subcomponente> ret = new ArrayList<Subcomponente>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Subcomponente> criteria = session.createQuery("SELECT c FROM Subcomponente c WHERE estado = 1 AND c.id in (SELECT u.id.subcomponenteid from SubcomponenteUsuario u where u.id.usuario=:usuario )",Subcomponente.class);
			criteria.setParameter("usuario", usuario);
			criteria.setFirstResult(((pagina-1)*(numeroSubComponentes)));
			criteria.setMaxResults(numeroSubComponentes);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalSubComponentes(String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(c.id) FROM Subcomponente c WHERE c.estado=1 AND  c.id in (SELECT u.id.subcomponenteid from SubcomponenteUsuario u where u.id.usuario=:usuario )",Long.class);
			conteo.setParameter("usuario", usuario);
			ret = conteo.getSingleResult();
		} catch(Throwable e){
			CLogger.write("7", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<Subcomponente> getSubComponentesPaginaPorComponente(int pagina, int numeroSubComponentes, int componenteId,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion, String columna_ordenada, String orden_direccion, String usuario){

		List<Subcomponente> ret = new ArrayList<Subcomponente>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{

			String query = "SELECT c FROM Subcomponente c WHERE estado = 1 AND c.componente.id = :compId ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query =String.join("",query," AND  c.id in (SELECT u.id.subcomponenteid from SubcomponenteUsuario u where u.id.usuario=:usuario ) ");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Subcomponente> criteria = session.createQuery(query,Subcomponente.class);
			criteria.setParameter("compId", componenteId);
			criteria.setParameter("usuario", usuario);
			criteria.setFirstResult(((pagina-1)*(numeroSubComponentes)));
			criteria.setMaxResults(numeroSubComponentes);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}


	public static Long getTotalSubComponentesPorComponente(int componenteId,
			String filtro_nombre,String filtro_usuario_creo,
			String filtro_fecha_creacion, String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{

			String query = "SELECT count(c.id) FROM Subcomponente c WHERE c.estado=1 AND c.componente.id = :compId ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = String.join("", query, " AND  c.id in (SELECT u.id.subcomponenteid from SubcomponenteUsuario u where u.id.usuario=:usuario )");
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("compId", componenteId);
			conteo.setParameter("usuario", usuario);
			ret = conteo.getSingleResult();
		}catch (NoResultException e){
			
		}catch(Throwable e){
			CLogger.write("9", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Subcomponente getSubComponenteInicial(Integer componenteId, String usuario, Session session){
		Subcomponente ret = null;
		List<Subcomponente> listRet = null;
		try{
			String query = "FROM Subcomponente c where c.estado=1 and c.orden=1 and c.componente.id=:componenteId and c.usuarioCreo=:usuario";
			Query<Subcomponente> criteria = session.createQuery(query, Subcomponente.class);
			criteria.setParameter("componenteId", componenteId);
			criteria.setParameter("usuario", usuario);
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("10", SubComponenteDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static Subcomponente getSubComponenteFechaMaxima(Integer componenteId, String usuario, Session session){
		Subcomponente ret = null;
		List<Subcomponente> listRet = null;
		try{
			String query = "FROM Subcomponente c where c.estado=1 and c.componente.id=:componenteId and c.usuarioCreo=:usuario order by c.fechaFin desc";
			Query<Subcomponente> criteria = session.createQuery(query, Subcomponente.class);
			criteria.setMaxResults(1);
			criteria.setParameter("componenteId", componenteId);
			criteria.setParameter("usuario", usuario);
			
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}catch (NoResultException e){
			
		} catch(Throwable e){
			CLogger.write("11", SubComponenteDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static List<Subcomponente> getSubComponentesOrden(Integer componenteId, String usuario, Session session){
		List<Subcomponente> ret = null;
		try{
			String query = String.join(" ", "SELECT c FROM Subcomponente c where c.estado=1 and c.componente.id=:componenteId");
			query = String.join(" ", query, "AND c.id in (SELECT u.id.subcomponenteid from SubcomponenteUsuario u where u.id.usuario=:usuario)");
			Query<Subcomponente> criteria = session.createQuery(query,Subcomponente.class);
			criteria.setParameter("componenteId", componenteId);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("12", SubComponenteDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static Subcomponente getSubComponentePorIdOrden(int id, String usuario, Session session){
		Subcomponente ret = null;
		List<Subcomponente> listRet = null;
		try{
			Query<Subcomponente> criteria = session.createQuery("FROM Subcomponente where id=:id AND id in (SELECT u.id.subcomponenteid from SubcomponenteUsuario u where u.id.usuario=:usuario )", Subcomponente.class);
			criteria.setParameter("id", id);
			criteria.setParameter("usuario", usuario);
			listRet = criteria.getResultList();
			 
			 ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("13", SubComponenteDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarSubComponenteOrden(Subcomponente SubComponente, Session session){
		boolean ret = false;
		try{
			session.saveOrUpdate(SubComponente);
			session.flush();
			session.clear();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("14", SubComponenteDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static Subcomponente getSubComponente(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Subcomponente ret = null;
		List<Subcomponente> listRet = null;
		try{
			Query<Subcomponente> criteria = session.createQuery("Select S FROM Subcomponente S where S.id=:id and S.estado = 1", Subcomponente.class);
			criteria.setParameter("id", id);
			 listRet = criteria.getResultList();
			 
			 ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("15", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static BigDecimal calcularCosto(Subcomponente subcomponente){
		BigDecimal costo = new BigDecimal(0);
		try{
			Set<Producto> productos = subcomponente.getProductos();
			List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(subcomponente.getId(), 2);
			if((productos != null && productos.size() > 0) || (actividades!=null && actividades.size()>0) ){
				if(productos!=null){
					Iterator<Producto> iterador = productos.iterator();
					
					while(iterador.hasNext()){
						Producto producto = iterador.next();
						costo = costo.add(producto.getCosto() != null ? producto.getCosto() : new BigDecimal(0));
					}
				}
				
				if(actividades != null && actividades.size() > 0){
					for(Actividad actividad : actividades){
						costo = costo.add(actividad.getCosto() != null ? actividad.getCosto() : new BigDecimal(0));
					}
				}
			}else{
				PlanAdquisicion pa = PlanAdquisicionDAO.getPlanAdquisicionByObjeto(2, subcomponente.getId());
				if(pa!=null){
						if(pa.getPlanAdquisicionPagos()!=null && pa.getPlanAdquisicionPagos().size()>0){
							BigDecimal pagos = new BigDecimal(0);
							for(PlanAdquisicionPago pago: pa.getPlanAdquisicionPagos())
								pagos.add(pago.getPago());
							costo = pagos;
						}
						else
							costo = pa.getMontoContrato();
				}
				else
					costo = subcomponente.getCosto();
			}
		}catch(Exception e){
			CLogger.write("16", Proyecto.class, e);
		} 
		
		return costo;
	}
	
	public static List<Subcomponente> getSubComponentesPorComponente(Integer componenteId){
		List<Subcomponente> ret = new ArrayList<Subcomponente>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Subcomponente> criteria = session.createQuery("FROM Subcomponente c where estado = 1 and c.componente.id = :componenteId order by c.id asc", Subcomponente.class);
			criteria.setParameter("componenteId", componenteId);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("19", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean calcularCostoyFechas(Integer subcomponenteId){
		boolean ret = false;
		ArrayList<ArrayList<Nodo>> listas = EstructuraProyectoDAO.getEstructuraObjetoArbolCalculos(subcomponenteId, 2);
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
					nodo.costo = calcularCosto((Subcomponente)nodo.objeto).doubleValue();
				nodo.duracion = Utils.getWorkingDays(new DateTime(nodo.fecha_inicio), new DateTime(nodo.fecha_fin));
				setDatosCalculados(nodo.objeto,nodo.fecha_inicio,nodo.fecha_fin,nodo.costo, nodo.duracion);
			}
			ret = true;
		}
		ret= ret && guardarSubComponenteBatch(listas);	
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
			CLogger.write("20", SubComponenteDAO.class, e);
		}
		
	}
	
	private static boolean guardarSubComponenteBatch(ArrayList<ArrayList<Nodo>> listas){
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
			CLogger.write("21", SubComponenteDAO.class, e);
		}
		return ret;
	}
	
	public static List<Subcomponente> getSubcomponentesPorComponenteHistory(Integer componenteId,String lineaBase){
		List<Subcomponente> ret = new ArrayList<Subcomponente>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select * ", 
					"from sipro_history.subcomponente c ",
					"where c.estado = 1 ",
					"and componenteid = ?1 ",
					lineaBase != null ? "and c.linea_base like '%" + lineaBase + "%'" : "and c.actual = 1",
							"order by c.id desc");
			Query<Subcomponente> criteria = session.createNativeQuery(query, Subcomponente.class);
			criteria.setParameter(1, componenteId);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("22", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Subcomponente getSubcomponenteHistory(Integer subcomponenteId,String lineaBase){
		Subcomponente ret = null;
		List<Subcomponente> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select * ", 
					"from sipro_history.subcomponente c ",
					"where c.estado = 1 ",
					"and c.id = ?1 ",
					lineaBase != null ? "and c.linea_base like '%" + lineaBase + "%'" : "and c.actual = 1",
							"order by c.id desc");
			Query<Subcomponente> criteria = session.createNativeQuery(query, Subcomponente.class);
			criteria.setParameter(1, subcomponenteId);
			listRet =   criteria.getResultList();
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("23", SubComponenteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static String getVersiones (Integer componenteId){
		String resultado = "";
		String query = "SELECT DISTINCT(version) "
				+ " FROM sipro_history.subcomponente "
				+ " WHERE id = "+componenteId;
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
	
	public static String getHistoria (Integer componenteId, Integer version){
		String resultado = "";
		String query = "SELECT c.version, c.nombre, c.descripcion, ct.nombre tipo, ue.nombre unidad_ejecutora, c.costo, ac.nombre tipo_costo, "
				+ " c.programa, c.subprograma, c.proyecto, c.actividad, c.obra, c.renglon, c.ubicacion_geografica, c.latitud, c.longitud, "
				+ " c.fecha_inicio, c.fecha_fin, c.duracion, c.fecha_inicio_real, c.fecha_fin_real, "
				+ " c.fecha_creacion, c.usuario_creo, c.fecha_actualizacion, c.usuario_actualizo, "
				+ " CASE WHEN c.estado = 1 "
				+ " THEN 'Activo' "
				+ " ELSE 'Inactivo' "
				+ " END AS estado "
				+ " FROM sipro_history.subcomponente c "
				+ " JOIN sipro.unidad_ejecutora ue ON c.unidad_ejecutoraunidad_ejecutora = ue.unidad_ejecutora and c.entidad = ue.entidadentidad and c.ejercicio = ue.ejercicio  JOIN sipro_history.subcomponente_tipo ct ON c.subcomponente_tipoid = ct.id "
				+ " JOIN sipro_history.acumulacion_costo ac ON c.acumulacion_costoid = ac.id "
				+ " WHERE c.id = "+componenteId
				+ " AND c.version = " +version;
		
		String [] campos = {"Version", "Nombre", "Descripción", "Tipo", "Unidad Ejecutora", "Monto Planificado", "Tipo Acumulación de Monto Planificado", 
				"Programa", "Subprograma", "Proyecto", "Actividad", "Obra", "Renglon", "Ubicación Geográfica", "Latitud", "Longitud", 
				"Fecha Inicio", "Fecha Fin", "Duración", "Fecha Inicio Real", "Fecha Fin Real", 
				"Fecha Creación", "Usuario que creo", "Fecha Actualización", "Usuario que actualizó", 
				"Estado"};
		resultado = CHistoria.getHistoria(query, campos);
		return resultado;
	}
}