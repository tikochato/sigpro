package dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Actividad;
import pojo.ActividadUsuario;
import pojo.ActividadUsuarioId;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

class duracionFecha{
	int id;
	int duracion;
	Date fecha_inicial;
	Date fecha_final;
	String dimension;
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	 
	public void setDuracion(int duracion){
		this.duracion = duracion;
	}
	
	public int getDuracion(){
		return this.duracion;
	} 
	 
	public void setFechaInicial(Date fechaInicial){
		this.fecha_inicial = fechaInicial;
	}
	
	public Date getFechaInicial(){
		return this.fecha_inicial;
	}
	
	public void setFechaFin(Date fechaFin){
		this.fecha_final = fechaFin;
	}
	
	public Date getFechaFin(){
		return this.fecha_final;
	}
	
	public void setDimension(String dimension){
		this.dimension = dimension;
	}
	
	public String getDimension(){
		return this.dimension;
	}
}

public class ActividadDAO {
	public static List<Actividad> getActividads(String usuario){
		List<Actividad> ret = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Actividad> criteria = session.createQuery("FROM Actividad p where p.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )", Actividad.class);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Actividad getActividadPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		List<Actividad> listRet = null;
		Actividad ret = null;
		try{
			String query = "FROM Actividad a where a.id=:id";
			Query<Actividad> criteria = session.createQuery(query, Actividad.class);
			criteria.setParameter("id", id);
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("2", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarActividad(Actividad Actividad, boolean calcular_valores_agregados){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			if(Actividad.getId()==null || Actividad.getId()<1){
				session.save(Actividad);
				session.flush();
				switch(Actividad.getObjetoTipo()){
					case 1:
						Proyecto proyecto = ProyectoDAO.getProyecto(Actividad.getObjetoId());
						Actividad.setTreePath(proyecto.getTreePath()+""+(10000000+Actividad.getId()));
						break;
					case 2:
						Componente componente = ComponenteDAO.getComponente(Actividad.getObjetoId());
						Actividad.setTreePath(componente.getTreePath()+""+(10000000+Actividad.getId()));
						break;
					case 3:
						Producto producto = ProductoDAO.getProductoPorId(Actividad.getObjetoId());
						Actividad.setTreePath(producto.getTreePath()+""+(10000000+Actividad.getId()));
						break;
					case 4:
						Subproducto subproducto = SubproductoDAO.getSubproductoPorId(Actividad.getObjetoId());
						Actividad.setTreePath(subproducto.getTreePath()+""+(10000000+Actividad.getId()));
						break;
					case 5:
						Actividad actividad = ActividadDAO.getActividadPorId(Actividad.getObjetoId());
						Actividad.setTreePath(actividad.getTreePath()+""+(10000000+Actividad.getId()));
						break;
				}
			}
			session.saveOrUpdate(Actividad);
			ActividadUsuario au = new ActividadUsuario(new ActividadUsuarioId(Actividad.getId(), Actividad.getUsuarioCreo()),Actividad);
			session.saveOrUpdate(au);

			if(calcular_valores_agregados){
				Actividad.setCosto(calcularActividadCosto(Actividad));
				Date fechaMinima = calcularFechaMinima(Actividad);
				Date fechaMaxima = calcularFechaMaxima(Actividad);
				Integer duracion = Utils.getWorkingDays(fechaMinima, fechaMaxima);
				Actividad.setFechaInicio(fechaMinima);
				Actividad.setFechaFin(fechaMaxima);
				Actividad.setDuracion(duracion);
				session.saveOrUpdate(Actividad);
				session.getTransaction().commit();
				session.close();
				actualizarCostoPapa(Actividad);
			}
			else{
				session.getTransaction().commit();
				session.close();
			}
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarActividad(Actividad Actividad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Actividad.setEstado(0);
			Actividad.setOrden(null);
			session.beginTransaction();
			session.update(Actividad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarTotalActividad(Actividad Actividad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(Actividad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<Actividad> getActividadsPagina(int pagina, int numeroActividads,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion, String usuario){
		List<Actividad> ret = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT c FROM Actividad c WHERE estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = String.join("", query, " AND c.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario ) ");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Actividad> criteria = session.createQuery(query,Actividad.class);
			criteria.setParameter("usuario", usuario);
			criteria.setFirstResult(((pagina-1)*(numeroActividads)));
			criteria.setMaxResults(numeroActividads);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalActividads(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion, String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(c.id) FROM Actividad c WHERE c.estado=1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join("", query, " AND c.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario ) ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("usuario", usuario);
			ret = conteo.getSingleResult();
		} catch(Throwable e){
			CLogger.write("7", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}


	public static List<Actividad> getActividadsPaginaPorObjeto(int pagina, int numeroActividads, int objetoId, int objetoTipo,String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion, String usuario){
		List<Actividad> ret = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM Actividad a WHERE a.estado = 1 AND a.objetoId = :objetoId AND a.objetoTipo = :objetoTipo ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " a.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " a.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(a.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, "AND a.estado=1 AND a.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query," ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Actividad> criteria = session.createQuery(query,Actividad.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			if (usuario!=null){
				criteria.setParameter("usuario", usuario);
			}
			criteria.setFirstResult(((pagina-1)*(numeroActividads)));
			criteria.setMaxResults(numeroActividads);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalActividadsPorObjeto(int objetoId, int objetoTipo, String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(a.id) FROM Actividad a WHERE a.estado=1 and a.objetoId=:objetoId and a.objetoTipo=:objetoTipo ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " a.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " a.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(a.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND a.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )");
			Query<Long> criteria = session.createQuery(query,Long.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getSingleResult();
		}catch(Throwable e){
			CLogger.write("9", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static String getFechaInicioFin(Actividad actividad, String usuario){
		List<duracionFecha> objetoActividadFechas = new ArrayList<duracionFecha>();
		String fecha = "";
		
		duracionFecha df = new duracionFecha();
		df.setId(actividad.getId());
		df.setDimension(actividad.getDuracionDimension());
		df.setDuracion(actividad.getDuracion());
		objetoActividadFechas.add(df);
		if(actividad.getPredObjetoId() != null && actividad.getPredObjetoId() != actividad.getId()){
			objetoActividadFechas = getPredecesora(getActividadPorId(actividad.getPredObjetoId()),usuario, objetoActividadFechas);
			
			Date fechaFI = null;
			for(int i = objetoActividadFechas.size()-1; i >= 0; i--){
				if(fechaFI != null)
					objetoActividadFechas.get(i).setFechaInicial(getFechaFinal(fechaFI,1,objetoActividadFechas.get(i).getDimension().charAt(0)));
				Date fechaI = objetoActividadFechas.get(i).fecha_inicial;
				objetoActividadFechas.get(i).setFechaFin(getFechaFinal(fechaI,objetoActividadFechas.get(i).getDuracion(),objetoActividadFechas.get(i).getDimension().charAt(0)));
				fechaFI = objetoActividadFechas.get(i).getFechaFin();
			}

			fecha = Utils.formatDate(objetoActividadFechas.get(0).getFechaInicial()) + ";" + Utils.formatDate(objetoActividadFechas.get(0).getFechaFin()); 
		}else{
			fecha = Utils.formatDate(actividad.getFechaInicio()) + ";" + Utils.formatDate(getFechaFinal(actividad.getFechaInicio(),actividad.getDuracion(),actividad.getDuracionDimension().charAt(0))); 
		}
		
		return fecha;
	}
	
	private static List<duracionFecha> getPredecesora(Actividad actividad, String usuario, List<duracionFecha> fechasPredecesoras){
		duracionFecha df = new duracionFecha();
		df.setId(actividad.getId());
		df.setDimension(actividad.getDuracionDimension());
		df.setDuracion(actividad.getDuracion());
		fechasPredecesoras.add(df);
		
		if(actividad.getPredObjetoId() != null && actividad.getPredObjetoId() != actividad.getId()){
			fechasPredecesoras = getPredecesora(getActividadPorId(actividad.getPredObjetoId()),usuario, fechasPredecesoras);
		}else{
			fechasPredecesoras.get(fechasPredecesoras.size()-1).setFechaInicial(actividad.getFechaInicio());
		}
		
		return fechasPredecesoras;
	}
	
	public static List<Actividad> getActividadsSubactividadsPorObjeto(int objetoId, int objetoTipo){
		List<Actividad> ret = new ArrayList<Actividad>();
		List<Actividad> subactividades = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM Actividad a WHERE a.estado = 1 AND a.objetoId = :objetoId AND a.objetoTipo = :objetoTipo ";
			Query<Actividad> criteria = session.createQuery(query,Actividad.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getResultList();
			for(Actividad actividad : ret){
				subactividades.addAll(getActividadsSubactividadsPorObjeto(actividad.getId(), 5));
			}
			ret.addAll(subactividades);			
		}
		catch(Throwable e){
			CLogger.write("10", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Actividad getFechasActividad(Actividad actividad){
		int predecesor = (actividad.getPredObjetoId()!= null)?actividad.getPredObjetoId():0; 
		if( predecesor > 0){
			Actividad actividad_pred = getActividadPorId(predecesor);
			actividad_pred = getFechasActividad(actividad_pred);
			Calendar fecha_final_pred = Calendar.getInstance();
			fecha_final_pred.setTime(actividad_pred.getFechaFin());
			fecha_final_pred.add(Calendar.DAY_OF_MONTH, 1);
			actividad.setFechaInicio(fecha_final_pred.getTime());
		}
		
		Date fechaInicio = actividad.getFechaInicio();
		if (fechaInicio != null && actividad.getDuracionDimension() != null && !actividad.getDuracionDimension().isEmpty()){
			Date fechaFinal = getFechaFinal(fechaInicio, actividad.getDuracion(), actividad.getDuracionDimension().charAt(0));
			actividad.setFechaFin(fechaFinal);
		}

		return actividad;
	}
	
	public static Date getFechaFinal(Date fecha_inicio, int duracion, char dimension){
		Calendar fecha_final = Calendar.getInstance();
		fecha_final.setTime(fecha_inicio);
		
		if (Character.toUpperCase(dimension) == 'D'){ //Restamos un día para validar que la fecha de inicio sea día hábil
			fecha_final.add(Calendar.DAY_OF_MONTH, -1);
		}
		Integer contador=0;
		while (contador < duracion){
			switch(Character.toUpperCase(dimension)){
				case 'D':  //día
					fecha_final.add(Calendar.DAY_OF_MONTH, 1);
					break;
				default: 
			}
			boolean esFechaHabil = esFechaHabil(fecha_final); 
			if (esFechaHabil) {
                contador++;
            }
		}
		return new Date(fecha_final.getTimeInMillis());
	}
	
	public static boolean esFechaHabil(Calendar fecha) {
	      switch (fecha.get(Calendar.DAY_OF_WEEK)){
	        case Calendar.SUNDAY:
	        	return false; 
	        case Calendar.SATURDAY:
	        	return false; 
	        default:
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 1 && fecha.get(Calendar.MONTH) == Calendar.JANUARY) //Año nuevo
	        	  return false;
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 1 && fecha.get(Calendar.MONTH) == Calendar.MARCH) //Día del Trabajo
	        	  return false;
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 15 && fecha.get(Calendar.MONTH) == Calendar.SEPTEMBER) //Independencia
	        	  return false;
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 20 && fecha.get(Calendar.MONTH) == Calendar.OCTOBER) //Revolución
	        	  return false;
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 1 && fecha.get(Calendar.MONTH) == Calendar.NOVEMBER) //Todos los Santos
	        	  return false;
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 25 && fecha.get(Calendar.MONTH) == Calendar.DECEMBER) //Navidad
	        	  return false;
	          break;
	        }
	        return true;
	}
	
	
	public static List<Actividad> getActividadsPorObjetos(Integer idPrestamo,int anio_inicio, int anio_fin){
		List<Actividad> ret = new ArrayList<Actividad>();
		
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select a.* ", 
							"from matriz_raci mr , asignacion_raci ar, actividad a ",
							"Where mr.id = ar.matriz_raciid",
							"and ar.objeto_id = a.id",
							"and ar.objeto_tipo = 5",
							"and mr.proyectoid = ?3",
							"and mr.estado = 1",
							"and ar.rol_raci = 'r'",
							"and year(a.fecha_fin ) between ?1 and ?2");
			Query<Actividad> criteria = session.createNativeQuery(query,Actividad.class);
			criteria.setParameter("1", anio_inicio);
			criteria.setParameter("2", anio_fin);
			criteria.setParameter("3", idPrestamo);
			ret = criteria.getResultList();
						
		}
		catch(Throwable e){
			CLogger.write("11", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<?> getActividadesTerminadas(String idPrestamos, String idComponentes, String idProductos,
			String idSubproductos, Integer colaboradorid, int anio_inicio, int anio_fin){
		List<?> ret= null;
		
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select year(t2.fecha_fin) anio, month(t2.fecha_fin) mes, count(t2.id) total",
						"from (",
							"select distinct ac.* ",
							"from actividad ac , (",
							    "select p.id, 1 objeto_tipo",
								"from proyecto p",
								"where p.id = ",idPrestamos,
								"union",
								"select c.id, 2 objeto_tipo",
								"from proyecto p",
								"left outer join componente c on c.proyectoid = p.id",
								"where c.id in (" + idComponentes + ")",
								"union",
								"select pr.id,3 objeto_tipo",
								"from proyecto p",
								"left outer join componente c on c.proyectoid = p.id",
								"left outer join producto pr on pr.componenteid = c.id",
								"where pr.id in ("+ idProductos + ")",
								"union ",
								"select s.id,4 objeto_tipo",
								"from proyecto p",
								"left outer join componente c on c.proyectoid = p.id",
								"left outer join producto pr on pr.componenteid = c.id",
								"left outer join subproducto s on s.productoid = pr.id",
								"where s.id in (" + idSubproductos + ")",							    
							    ") t1, asignacion_raci ar",
							"where ac.objeto_id = t1.id",
							"and (ac.id = ar.objeto_id and ar.objeto_tipo = 5)",
							"and ac.objeto_tipo = t1.objeto_tipo",
							"and ac.estado = 1",
							(colaboradorid != null && colaboradorid > 0 ?  "and ar.colaboradorid = ?1" : ""),
							"and year(ac.fecha_fin ) between ?2 and ?3",
							"and porcentaje_avance = 100",
							") t2",
							"group by year(t2.fecha_fin) , month(t2.fecha_fin) asc");
			Query<?> criteria = session.createNativeQuery(query);
			if (colaboradorid != null && colaboradorid > 0)
				criteria.setParameter("1", colaboradorid);
			criteria.setParameter("2", anio_inicio);
			criteria.setParameter("3", anio_fin);
			ret = criteria.getResultList();
			
		}
		catch(Throwable e){
			CLogger.write("12", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Actividad getActividadPorIdResponsable(int id, String responsables,String rol){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Actividad ret = null;
		List<Actividad> listRet = null;
		try{
			String query = String.join(" ", "select a.*",
				"from actividad a,asignacion_raci ar",
				"where a.id = ar.objeto_id ",
				"and ar.objeto_tipo = 5",
				"and a.estado = 1",
				"and a.id = ?1",
				"and ar.colaboradorid in (",responsables ,")",
				"and ar.rol_raci = ?3");
			Query<Actividad> criteria = session.createNativeQuery(query, Actividad.class);
			criteria.setParameter("1", id);
			criteria.setParameter("3", rol);
			
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("13", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static Actividad getActividadInicial(Integer objetoId, Integer objetoTipo, String usuario){
		Actividad ret = null;
		List<Actividad> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "FROM Actividad a where a.estado=1 and a.orden=1 and a.objetoId=:objetoId and a.objetoTipo=:objetoTipo and a.usuarioCreo=:usuario";
			Query<Actividad> criteria = session.createQuery(query, Actividad.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			criteria.setParameter("usuario", usuario);
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("15", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Actividad getActividadFechaMaxima(Integer objetoId, Integer objetoTipo, String usuario){
		Actividad ret = null;
		List<Actividad> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "FROM Actividad a where a.estado=1 and a.objetoId=:objetoId and a.objetoTipo=:objetoTipo and a.usuarioCreo=:usuario order by a.fechaFin desc";
			Query<Actividad> criteria = session.createQuery(query, Actividad.class);
			criteria.setMaxResults(1);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			criteria.setParameter("usuario", usuario);
			
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
			
		} catch(Throwable e){
			CLogger.write("16", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Actividad getActividadPorIdOrden(int id, String usuario, Session session){
		Actividad ret = null;
		List<Actividad> listRet = null;
		try{
			String query = "FROM Actividad where id=:id";
			if (usuario != null){
				query += " AND id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )";
			}
			Query<Actividad> criteria = session.createQuery(query, Actividad.class);
			criteria.setParameter("id", id);
			if(usuario != null){
			criteria.setParameter("usuario", usuario);
			}
			
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
			
		} catch(Throwable e){
			CLogger.write("17", ActividadDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarActividadOrden(Actividad Actividad, Session session){
		boolean ret = false;
		try{
			session.saveOrUpdate(Actividad);
			session.flush();
			session.clear();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("18", ActividadDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static List<Actividad> getActividadesPorObjeto(Integer objetoId, Integer objetoTipo){
		List<Actividad> ret = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM Actividad a WHERE a.estado = 1 AND a.objetoId = :objetoId AND a.objetoTipo = :objetoTipo ";
			Query<Actividad> criteria = session.createQuery(query,Actividad.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("19", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
		
	public static BigDecimal calcularActividadCosto(Actividad actividad){
		BigDecimal costo = new BigDecimal(0);
		List<Actividad> subactividades = getActividadesPorObjeto(actividad.getId(), 5);
		if(subactividades!=null && subactividades.size()>0){
			Iterator<Actividad> actual = subactividades.iterator();
			while (actual.hasNext()) {
				Actividad hija = actual.next();
				BigDecimal costoHija = calcularActividadCosto(hija);
				costo = costo.add(costoHija!=null ? costoHija : new BigDecimal(0));
			}
		}else{
			costo = actividad.getCosto();
			costo = costo!=null ? costo : new BigDecimal(0);
		}
		return costo;
	}
	
	public static Date calcularFechaMinima(Actividad actividad){
		Date fecha = null;
		List<Actividad> subactividades = getActividadesPorObjeto(actividad.getId(), 5);
		if(subactividades!=null && subactividades.size()>0){
			Iterator<Actividad> actual = subactividades.iterator();
			while (actual.hasNext()){
				Actividad hija = actual.next();
				Date fechaHija = calcularFechaMinima(hija);
				if(fecha==null || fechaHija.before(fecha)){
					fecha = fechaHija;
				}
			}
		}
		fecha = fecha!=null ? fecha : actividad.getFechaInicio();
		return fecha;
	}
	
	public static Date calcularFechaMaxima(Actividad actividad){
		Date fecha = null;
		List<Actividad> subactividades = getActividadesPorObjeto(actividad.getId(), 5);
		if(subactividades!=null && subactividades.size()>0){
			Iterator<Actividad> actual = subactividades.iterator();
			while (actual.hasNext()){
				Actividad hija = actual.next();
				Date fechaHija = calcularFechaMaxima(hija);
				if(fecha==null || fechaHija.after(fecha)){
					fecha = fechaHija;
				}
			}
		}
		fecha = fecha!=null ? fecha : actividad.getFechaFin();
		return fecha;
	}
	
	public static void actualizarCostoPapa(Actividad actividad){
		switch(actividad.getObjetoTipo()){
			case 1:
				Proyecto proyecto = ProyectoDAO.getProyecto(actividad.getObjetoId());
				ProyectoDAO.guardarProyecto(proyecto, true); 
				break;
			case 2:
				Componente componente = ComponenteDAO.getComponente(actividad.getObjetoId());
				ComponenteDAO.guardarComponente(componente, true); 
				break;
			case 3:
				Producto producto = ProductoDAO.getProductoPorId(actividad.getObjetoId());
				ProductoDAO.guardarProducto(producto, true);
				break;
			case 4: 
				Subproducto subproducto = SubproductoDAO.getSubproductoPorId(actividad.getObjetoId());
				SubproductoDAO.guardarSubproducto(subproducto, true);
				break;
			case 5:
				Actividad padre = getActividadPorId(actividad.getObjetoId());
				guardarActividad(padre, true);
		}
	}
	
	
	public static List<Actividad> obtenerActividadesHijas(Integer proyectoId){
		List<Actividad> ret = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM Actividad a WHERE a.id IN (" +
					"select left(a1.treePath,length(a1.treePath)-8) papa, min(a1.id) "+
					"from actividad a1 " +
					"where a1.treePath like '"+(10000000+proyectoId)+"%' "+
					"and not exists (select * from actividad a2 where a2.objeto_id=a1.id and a2.objeto_tipo=5) " +
					"group by left(a1.treePath,length(a1.treePath)-8) "
					+ ") ";
			Query<Actividad> criteria = session.createQuery(query,Actividad.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("20", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
}
