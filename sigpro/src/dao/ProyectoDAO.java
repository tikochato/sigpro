package dao;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.LockModeType;
import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;

import pojo.Actividad;
import pojo.Componente;
import pojo.PepDetalle;
import pojo.Proyecto;
import pojo.ProyectoUsuario;
import pojo.ProyectoUsuarioId;
import pojo.Usuario;
import utilities.CHibernateSession;
import utilities.CHistoria;
import utilities.CLogger;
import utilities.Utils;

public class ProyectoDAO implements java.io.Serializable  {


	private static final long serialVersionUID = 1L;

	public static List<Proyecto> getProyectos(String usuario){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto p where p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario ) and p.estado=1", Proyecto.class);
			criteria.setParameter("usuario", usuario);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Proyecto> getProyectos(Integer prestamoId, String usuario){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto p where p.prestamo.id=:prestamo and p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario ) and p.estado=1", Proyecto.class);
			criteria.setParameter("prestamo", prestamoId);
			criteria.setParameter("usuario", usuario);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("2", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarProyecto(Proyecto proyecto,boolean calcular_valores_agregados){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			if(proyecto.getId()==null || proyecto.getId()<1){
				session.saveOrUpdate(proyecto);
				session.flush();
				proyecto.setTreePath((10000000+proyecto.getId())+"");
			}
			Usuario usu = UsuarioDAO.getUsuario( proyecto.getUsuarioCreo());
			ProyectoUsuario pu = new ProyectoUsuario(new ProyectoUsuarioId(proyecto.getId(),proyecto.getUsuarioCreo()), proyecto,usu);
			session.saveOrUpdate(pu);
			if(!proyecto.getUsuarioCreo().equals("admin")){
				ProyectoUsuario pu_admin = new ProyectoUsuario(new ProyectoUsuarioId(proyecto.getId(),"admin"), proyecto,usu);
				session.saveOrUpdate(pu_admin);
			}
			session.saveOrUpdate(proyecto);
			session.getTransaction().commit();
			session.close();
			
			if(calcular_valores_agregados){
				calcularCostoyFechas(proyecto.getId());
			}

			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Proyecto getProyectoPorId(int id, String usuario){

		Session session = CHibernateSession.getSessionFactory().openSession();
		Proyecto ret = null;
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto where id=:id AND id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario )", Proyecto.class);
			criteria.setParameter("id", id);
			criteria.setParameter("usuario", usuario);
			 ret = criteria.getSingleResult();
		} catch (NoResultException e){
			
		}
		catch(Throwable e){
			CLogger.write("4", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static Proyecto getProyecto(int id){

		Session session = CHibernateSession.getSessionFactory().openSession();
		Proyecto ret = null;
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto where id=:id", Proyecto.class);
			criteria.setParameter("id", id);
			 ret = criteria.getSingleResult();
		} catch (NoResultException e){
		}
		catch(Throwable e){
			CLogger.write("5", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarProyecto(Proyecto proyecto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			proyecto.setEstado(0);
			session.beginTransaction();
			session.update(proyecto);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalProyectos(String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String usuario, Integer prestamoId){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM Proyecto p WHERE p.estado=1 ";
			String query_a="";
			if(prestamoId != null && prestamoId > 0)
				query_a = String.join("", " p.prestamo.id=", prestamoId+"");
			if(prestamoId == null)
				query_a = String.join("", " p.prestamo.id=null");
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario )");
			Query<Long> criteria = session.createQuery(query,Long.class);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<Proyecto> getProyectosPagina(int pagina, int numeroproyecto,
			String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion, String usuario, Integer prestamoId){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT p FROM Proyecto p WHERE p.estado = 1";
			String query_a="";
			if(prestamoId != null && prestamoId > 0)
				query_a = String.join("", " p.prestamo.id=", prestamoId+"");
			if(prestamoId == null)
				query_a = String.join("", " p.prestamo.id=null");
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario )");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) :
						String.join(" ", query, "ORDER BY fecha_creacion ASC");
			
			Query<Proyecto> criteria = session.createQuery(query,Proyecto.class);
			criteria.setParameter("usuario", usuario);
			criteria.setFirstResult(((pagina-1)*(numeroproyecto)));
			criteria.setMaxResults(numeroproyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Proyecto> getProyectosPaginaDisponibles(int pagina, int numeroproyecto,
			String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion,String idsProyectos){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT p FROM Proyecto p WHERE p.estado = 1";
			if (idsProyectos!=null && idsProyectos.trim().length()>0)
				query = String.join("", query," AND p.id not in (" + idsProyectos + ")");
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<Proyecto> criteria = session.createQuery(query,Proyecto.class);
			criteria.setFirstResult(((pagina-1)*(numeroproyecto)));
			criteria.setMaxResults(numeroproyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("9", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalProyectosDisponibles(String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String idsProyectos){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM Proyecto p WHERE p.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(idsProyectos!=null && idsProyectos.trim().length()>0)
				query = String.join("", query, " AND p.id not in ("+idsProyectos + " )");
			Query<Long> criteria = session.createQuery(query,Long.class);
			ret = criteria.getSingleResult();
		} catch (NoResultException e){
		}
		catch(Throwable e){
			CLogger.write("10", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Proyecto> getProyectosPorPrograma(int idPrograma){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Proyecto> criteria = session.createQuery("select p from Proyecto p "
					+ "inner join p.programaProyectos pp "
					+ "where pp.estado = 1 "
					+ "and pp.id.programaid = :idProg", Proyecto.class);
			
			criteria.setParameter("idProg", idPrograma);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("11", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}

	public static List<Proyecto> getProyectosPorUnidadEjecutora(String usuario, int unidadEjecutoraId){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Proyecto> criteria = session.createQuery("select p from Proyecto p "
					+ "inner join p.unidadEjecutora pp "
					+ "where p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario ) "
					+ "and p.estado=1 and pp.unidadEjecutora=:unidadEjecutora", Proyecto.class);
			criteria.setParameter("usuario", usuario);
			criteria.setParameter("unidadEjecutora", unidadEjecutoraId);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("12", Proyecto.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}
	
	public static Proyecto getProyectoPorUnidadEjecutora(int unidadEjecutoraId, int prestamoId, Integer entidad){
		Proyecto ret = null;
		List<Proyecto> listRet = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "select p from Proyecto p "
					+ "inner join p.unidadEjecutora pp "
					+ "where p.estado=1 "
					+ "and p.prestamo.id=:prestamoId "
					+ "and pp.id.unidadEjecutora=:unidadEjecutora ";			
			query += (entidad!=null && entidad>0) ? "and pp.entidad.id.entidad=:entidad " : " ";
			
			Query<Proyecto> criteria = session.createQuery(query, Proyecto.class);
			criteria.setParameter("unidadEjecutora", unidadEjecutoraId);
			criteria.setParameter("prestamoId", prestamoId);
			if(entidad!=null && entidad>0)
				criteria.setParameter("entidad", entidad);
			listRet =   criteria.getResultList();

			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("12", Proyecto.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}
	
	public static Proyecto getProyectoOrden(int id, Session session){
		Proyecto ret = null;
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto where id=:id", Proyecto.class).setLockMode(LockModeType.PESSIMISTIC_READ);
			criteria.setParameter("id", id);
			 ret = criteria.getSingleResult();
		} catch (NoResultException e){
		}
		catch(Throwable e){
			CLogger.write("13", ProyectoDAO.class, e);
		}
		return ret;
	}
	
	public static boolean guardarProyectoOrden(Proyecto proyecto, Session session){
		boolean ret = false;
		try{
			session.saveOrUpdate(proyecto);
			session.flush();
			session.clear();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("14", ProyectoDAO.class, e);
			session.getTransaction().rollback();
			session.close();
		}
		return ret;
	}
	
	public static List<Proyecto> getTodosProyectos(){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Proyecto> criteria = session.createQuery("FROM Proyecto p where p.estado=1", Proyecto.class);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("15", Proyecto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static BigDecimal calcularCosto(Proyecto proyecto){
		BigDecimal costo = new BigDecimal(0);
		try{
			Set<Componente> componentes = proyecto.getComponentes();
			List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(proyecto.getId(), 1);
			
			if((componentes != null && componentes.size() > 0) || (actividades!=null && actividades.size()>0)){
				Iterator<Componente> iterador = componentes.iterator();
				
				while(iterador.hasNext()){
					Componente componente = iterador.next();
					costo = costo.add(componente.getCosto() != null ? componente.getCosto() : new BigDecimal(0));
				}
					
				if(actividades != null && actividades.size() > 0){
					for(Actividad actividad : actividades){
						costo = costo.add(actividad.getCosto() != null ? actividad.getCosto() : new BigDecimal(0));
					}
				}			
			}else{
				costo = proyecto.getCosto();
			}				
		}catch(Exception e){
			CLogger.write("16", Proyecto.class, e);
		} 
		
		return costo;
	}
	
	public static Date calcularFechaMinima(Proyecto proyecto){
		DateTime fechaActual = null;
		try{
			List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(proyecto.getId(), 1);
			if(actividades != null && actividades.size() > 0){
				DateTime fechaMinima = new DateTime();
				for(Actividad actividad : actividades){
					if(fechaActual == null)
						fechaActual = new DateTime(actividad.getFechaInicio());
					else{
						fechaMinima = new DateTime(actividad.getFechaInicio());
						
						if(fechaActual.isAfter(fechaMinima))
							fechaActual = fechaMinima;
					}
				}
			}
			
			Set<Componente> componentes = proyecto.getComponentes();
			if(componentes != null && componentes.size() > 0){
				Iterator<Componente> iterador = componentes.iterator();
				DateTime fechaMinima = new DateTime();
				while(iterador.hasNext()){
					Componente componente = iterador.next();
					if(fechaActual == null)
						fechaActual = new DateTime(componente.getFechaInicio());
					else{
						fechaMinima = new DateTime(componente.getFechaInicio());
						
						if(fechaActual.isAfter(fechaMinima))
							fechaActual = fechaMinima;
					}
				}	
			}else if(fechaActual == null)
				fechaActual = new DateTime(proyecto.getFechaInicio());	
		}catch(Exception e){
			CLogger.write("17", Proyecto.class, e);
		}
		
		return fechaActual.toDate();
	}

	public static Date calcularFechaMaxima(Proyecto proyecto){
		DateTime fechaActual = null;
		try{
			List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(proyecto.getId(), 1);
			if(actividades != null && actividades.size() > 0){
				DateTime fechaMaxima = new DateTime();
				for(Actividad actividad : actividades){
					if(fechaActual == null)
						fechaActual = new DateTime(actividad.getFechaFin());
					else{
						fechaMaxima = new DateTime(actividad.getFechaFin());
						
						if(fechaActual.isBefore(fechaMaxima))
							fechaActual = fechaMaxima;
					}
				}
			}
			
			Set<Componente> componentes = proyecto.getComponentes();
			if(componentes != null && componentes.size() > 0){
				Iterator<Componente> iterador = componentes.iterator();
				DateTime fechaMaxima = new DateTime();
				while(iterador.hasNext()){
					Componente componente = iterador.next();
					if(fechaActual == null)
						fechaActual = new DateTime(componente.getFechaFin());
					else{
						fechaMaxima = new DateTime(componente.getFechaFin());
						
						if(fechaActual.isBefore(fechaMaxima))
							fechaActual = fechaMaxima;
					}
				}
			}else if(fechaActual == null)
					fechaActual = new DateTime(proyecto.getFechaFin());
				
		}catch(Exception e){
			CLogger.write("18", Proyecto.class, e);
		}
		
		return fechaActual.toDate();
	}
	
	public static boolean calcularCostoyFechas(Integer proyectoId){
		boolean ret = false;
		ArrayList<ArrayList<Nodo>> listas = EstructuraProyectoDAO.getEstructuraProyectoArbolCalculos(proyectoId, null);
		for(int i=listas.size()-1; i>=0; i--){
			for(int j=0; j<listas.get(i).size(); j++){
				Nodo nodo = listas.get(i).get(j);
				Double costo=0.0d;
				Timestamp fecha_maxima=new Timestamp(0);
				Timestamp fecha_minima=new Timestamp((new DateTime(2999,12,31,0,0,0)).getMillis());
				Timestamp fecha_maxima_real=null;
				Timestamp fecha_minima_real=null;
				for(Nodo nodo_hijo:nodo.children){
					costo += nodo_hijo.costo;
					fecha_minima = (nodo_hijo.fecha_inicio.getTime()<fecha_minima.getTime()) ? nodo_hijo.fecha_inicio : fecha_minima;
					fecha_maxima = (nodo_hijo.fecha_fin.getTime()>fecha_maxima.getTime()) ? nodo_hijo.fecha_fin : fecha_maxima;
					fecha_minima_real = nodo_hijo.fecha_inicio_real != null ? fecha_minima_real != null ? ((nodo_hijo.fecha_inicio_real.getTime()<fecha_minima_real.getTime()) ? nodo_hijo.fecha_inicio_real : fecha_minima_real) : nodo_hijo.fecha_inicio_real : fecha_minima_real != null ? fecha_minima_real : fecha_minima_real;
					fecha_maxima_real = nodo_hijo.fecha_fin_real != null ? fecha_maxima_real != null ? ((nodo_hijo.fecha_fin_real.getTime() > fecha_maxima_real.getTime()) ? nodo_hijo.fecha_fin_real : fecha_maxima_real) : nodo_hijo.fecha_fin_real : fecha_maxima_real != null ? fecha_maxima_real : null;
				}
				nodo.objeto = ObjetoDAO.getObjetoPorIdyTipo(nodo.id, nodo.objeto_tipo);
				if(nodo.children!=null && nodo.children.size()>0){
					nodo.fecha_inicio = fecha_minima;
					nodo.fecha_fin = fecha_maxima;
					nodo.fecha_inicio_real = fecha_minima_real;
					nodo.fecha_fin_real = fecha_maxima_real;
					nodo.costo = costo;
				}
				else{
					BigDecimal costo_temp= ObjetoDAO.calcularCostoPlan(nodo.objeto, nodo.objeto_tipo);
					nodo.costo = (costo_temp!=null) ? costo_temp.doubleValue(): 0;
				}
				nodo.duracion = Utils.getWorkingDays(new DateTime(nodo.fecha_inicio), new DateTime(nodo.fecha_fin));
				setDatosCalculados(nodo.objeto,nodo.fecha_inicio,nodo.fecha_fin,nodo.costo, nodo.duracion, nodo.fecha_inicio_real, nodo.fecha_fin_real);
			}
			ret = true;
		}
		ret= ret && guardarProyectoBatch(listas);	
		return ret;
	}
	
	private static void setDatosCalculados(Object objeto,Timestamp fecha_inicio, Timestamp fecha_fin, Double costo, int duracion, Timestamp fecha_inicio_real, Timestamp fecha_fin_real){
		try{
			if(objeto!=null){
				Method setFechaInicio =objeto.getClass().getMethod("setFechaInicio",Date.class);
				Method setFechaFin =  objeto.getClass().getMethod("setFechaFin",Date.class);
				Method setCosto = objeto.getClass().getMethod("setCosto",BigDecimal.class);
				Method setDuracion = objeto.getClass().getMethod("setDuracion", int.class);
				Method setFechaInicioReal = objeto.getClass().getMethod("setFechaInicioReal", Date.class);
				Method setFechaFinReal = objeto.getClass().getMethod("setFechaFinReal", Date.class);
				if(fecha_inicio!=null)
					setFechaInicio.invoke(objeto, new Date(fecha_inicio.getTime()));
				if(fecha_fin!=null)
					setFechaFin.invoke(objeto, new Date(fecha_fin.getTime()));
				if(costo!=null)
					setCosto.invoke(objeto, new BigDecimal(costo));
				setDuracion.invoke(objeto, duracion);
				if(fecha_inicio_real!=null)
					setFechaInicioReal.invoke(objeto, new Date(fecha_inicio_real.getTime()));
				if(fecha_fin_real!=null)
					setFechaFinReal.invoke(objeto, new Date(fecha_fin_real.getTime()));
			}
		}
		catch(Throwable e){
			CLogger.write("19", ProyectoDAO.class, e);
		}
		
	}
	
	private static boolean guardarProyectoBatch(ArrayList<ArrayList<Nodo>> listas){
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
			CLogger.write("20", ProyectoDAO.class, e);
		}
		return ret;
	}
	
	public static PepDetalle getPepDetalle(int id){

		Session session = CHibernateSession.getSessionFactory().openSession();
		PepDetalle ret = null;
		try{
			List<PepDetalle> listRet = null;
			Query<PepDetalle> criteria = session.createQuery("FROM PepDetalle p where p.id=:id and p.estado = 1", PepDetalle.class);
			criteria.setParameter("id", id);
			listRet=criteria.getResultList();
			 ret =!listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("21", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarPepDetalle(PepDetalle pepDetalle){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(pepDetalle);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("22", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	
	public static Proyecto getProyectoHistory(int id,String lineaBase){

		Session session = CHibernateSession.getSessionFactory().openSession();
		Proyecto ret = null;
		try{
			String query = String.join(" ", "select * from sipro_history.proyecto p " ,
					"where p.estado = 1  ",
					"and  p.id = ?1 ",
					lineaBase != null ? "and p.linea_base like '%" + lineaBase + "%'" : "and p.actual = 1 ");
			Query<Proyecto> criteria = session.createNativeQuery(query, Proyecto.class);
			criteria.setParameter(1, id);
			 ret = criteria.getSingleResult();
		} catch (NoResultException e){
		}
		catch(Throwable e){
			CLogger.write("21", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Proyecto> getProyectosPorPrestamoHistory(int idPrograma,String lineaBase){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ","select * ",
					"from sipro_history.proyecto p",
					"where p.prestamoid = ?1",
					"and p.estado = 1",
					lineaBase != null ? "and p.linea_base = ?2" : "and p.actual = 1");
			
			Query<Proyecto> criteria = session.createNativeQuery(query, Proyecto.class);
			
			criteria.setParameter(1, idPrograma);
			if (lineaBase != null)
				criteria.setParameter(2, lineaBase);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("22", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}
	
	public static Proyecto getProyectobyTreePath(String treePath){
		Proyecto ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String treePathProyecto = treePath.substring(0,8);
			Integer proyectoId = Utils.String2Int(treePathProyecto) - 10000000;
			ret = getProyecto(proyectoId);
		}catch(Exception e){
			CLogger.write("23", ProyectoDAO.class, e);
		}finally{
			session.close();
		}
		
		return ret;
	}
	
	public static String getVersiones (Integer productoId){
		String resultado = "";
		String query = "SELECT DISTINCT(version) "
				+ " FROM sipro_history.proyecto "
				+ " WHERE id = "+productoId;
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
	
	public static String getHistoria (Integer productoId, Integer version){
		String resultado = "";
		String query = "SELECT c.version, c.nombre, c.descripcion, ct.nombre tipo, ue.nombre unidad_ejecutora, c.costo, ac.nombre tipo_costo, "
				+ " c.programa, c.subprograma, c.proyecto, c.actividad, c.obra, c.renglon, c.ubicacion_geografica, c.latitud, c.longitud, "
				+ " c.fecha_inicio, c.fecha_fin, c.duracion, c.fecha_inicio_real, c.fecha_fin_real, "
				+ " c.fecha_creacion, c.usuario_creo, c.fecha_actualizacion, c.usuario_actualizo, "
				+ " CASE WHEN c.estado = 1 "
				+ " THEN 'Activo' "
				+ " ELSE 'Inactivo' "
				+ " END AS estado, ejecucion_fisica_real "
				+ " FROM sipro_history.proyecto c "
				+ " JOIN sipro.unidad_ejecutora ue ON c.unidad_ejecutoraunidad_ejecutora = ue.unidad_ejecutora and c.entidad = ue.entidadentidad and c.ejercicio = ue.ejercicio  JOIN sipro_history.proyecto_tipo ct ON c.proyecto_tipoid = ct.id "
				+ " LEFT JOIN sipro_history.acumulacion_costo ac ON c.acumulacion_costoid = ac.id "
				+ " WHERE c.id = "+productoId
				+ " AND c.version = " +version;
		
		String [] campos = {"Version", "Nombre", "Descripción", "Tipo", "Unidad Ejecutora", "Monto Planificado", "Tipo Acumulación de Monto Planificado", 
				"Programa", "Subprograma", "Proyecto", "Actividad", "Obra", "Renglon", "Ubicación Geográfica", "Latitud", "Longitud", 
				"Fecha Inicio", "Fecha Fin", "Duración", "Fecha Inicio Real", "Fecha Fin Real", 
				"Fecha Creación", "Usuario que creo", "Fecha Actualización", "Usuario que actualizó", 
				"Estado", "Ejecución Física %"};
		resultado = CHistoria.getHistoria(query, campos);
		return resultado;
	}
}
