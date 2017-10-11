package dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.LockModeType;
import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Actividad;
import pojo.Componente;
import pojo.Proyecto;
import pojo.ProyectoUsuario;
import pojo.ProyectoUsuarioId;
import pojo.Usuario;
import utilities.CHibernateSession;
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
			if(calcular_valores_agregados){
				proyecto.setCosto(ProyectoDAO.calcularCosto(proyecto));
				Date fechaMinima = calcularFechaMinima(proyecto);
				Date fechaMaxima = calcularFechaMaxima(proyecto);
				if(fechaMinima!=null && fechaMaxima!=null){
					Integer duracion = Utils.getWorkingDays(fechaMinima, fechaMaxima);
					proyecto.setDuracion(duracion);
				}
				proyecto.setFechaInicio(fechaMinima);
				proyecto.setFechaFin(fechaMaxima);
			}

			session.saveOrUpdate(proyecto);
			session.getTransaction().commit();
			session.close();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", ProyectoDAO.class, e);
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
			CLogger.write("3", ProyectoDAO.class, e);
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
			CLogger.write("3", ProyectoDAO.class, e);
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
			CLogger.write("4", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalProyectos(String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String usuario){
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
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.proyectoid from ProyectoUsuario u where u.id.usuario=:usuario )");
			Query<Long> criteria = session.createQuery(query,Long.class);
			criteria.setParameter("usuario", usuario);
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

	public static List<Proyecto> getProyectosPagina(int pagina, int numeroproyecto,
			String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion, String usuario){
		List<Proyecto> ret = new ArrayList<Proyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT p FROM Proyecto p WHERE p.estado = 1";
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
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) :
						String.join(" ", query, "ORDER BY fecha_creacion ASC");
			
			Query<Proyecto> criteria = session.createQuery(query,Proyecto.class);
			criteria.setParameter("usuario", usuario);
			criteria.setFirstResult(((pagina-1)*(numeroproyecto)));
			criteria.setMaxResults(numeroproyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", Proyecto.class, e);
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
			CLogger.write("7", Proyecto.class, e);
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
			CLogger.write("8", ProyectoDAO.class, e);
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
			CLogger.write("9", ProyectoDAO.class, e);
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
			CLogger.write("10", Proyecto.class, e);
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
			CLogger.write("11", ProyectoDAO.class, e);
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
			CLogger.write("12", ProyectoDAO.class, e);
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
			CLogger.write("13", Proyecto.class, e);
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
			if(componentes != null && componentes.size() > 0){
				Iterator<Componente> iterador = componentes.iterator();
				
				while(iterador.hasNext()){
					Componente componente = iterador.next();
					costo = costo.add(componente.getCosto() != null ? componente.getCosto() : new BigDecimal(0));
				}
					
				List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(proyecto.getId(), 1);
				if(actividades != null && actividades.size() > 0){
					for(Actividad actividad : actividades){
						costo = costo.add(actividad.getCosto() != null ? actividad.getCosto() : new BigDecimal(0));
					}
				}			
			}else{
				List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(proyecto.getId(), 1);
				if(actividades != null && actividades.size() > 0){
					for(Actividad actividad : actividades){
						costo = costo.add(actividad.getCosto() != null ? actividad.getCosto() : new BigDecimal(0));
					}
				}else
					costo = proyecto.getCosto() != null ? proyecto.getCosto() : new BigDecimal(0);
			}				
		}catch(Exception e){
			CLogger.write("16", Proyecto.class, e);
		} 
		
		return costo;
	}
	
	public static Date calcularFechaMinima(Proyecto proyecto){
		Date ret = null;
		try{
			Set<Componente> componentes = proyecto.getComponentes();
			if(componentes != null && componentes.size() > 0){
				Iterator<Componente> iterador = componentes.iterator();
				Date fechaMinima = new Date();
				while(iterador.hasNext()){
					Componente componente = iterador.next();
					if(ret == null)
						ret = componente.getFechaInicio();
					else{
						fechaMinima = componente.getFechaInicio();
						
						if(ret.after(fechaMinima))
							ret = fechaMinima;
					}
				}
				
				List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(proyecto.getId(), 1);
				if(actividades != null && actividades.size() > 0){
					fechaMinima = new Date();
					for(Actividad actividad : actividades){
						if(ret == null)
							ret = actividad.getFechaInicio();
						else{
							fechaMinima = actividad.getFechaInicio();
							
							if(ret.after(fechaMinima))
								ret = fechaMinima;
						}
					}
				}
			}else{
				List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(proyecto.getId(), 1);
				if(actividades != null && actividades.size() > 0){
					Date fechaMinima = new Date();
					for(Actividad actividad : actividades){
						if(ret == null)
							ret = actividad.getFechaInicio();
						else{
							fechaMinima = actividad.getFechaInicio();
							
							if(ret.after(fechaMinima))
								ret = fechaMinima;
						}
					}
				}else
					ret = proyecto.getFechaInicio();
			}
				
		}catch(Exception e){
			CLogger.write("17", Proyecto.class, e);
		}
		
		return ret;
	}

	public static Date calcularFechaMaxima(Proyecto proyecto){
		Date ret = null;
		try{
			Set<Componente> componentes = proyecto.getComponentes();
			if(componentes != null && componentes.size() > 0){
				Iterator<Componente> iterador = componentes.iterator();
				Date fechaMaxima = new Date();
				while(iterador.hasNext()){
					Componente componente = iterador.next();
					if(ret == null)
						ret = componente.getFechaFin();
					else{
						fechaMaxima = componente.getFechaFin();
						
						if(ret.before(fechaMaxima))
							ret = fechaMaxima;
					}
				}
				
				List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(proyecto.getId(), 1);
				if(actividades != null && actividades.size() > 0){
					fechaMaxima = new Date();
					for(Actividad actividad : actividades){
						if(ret == null)
							ret = actividad.getFechaFin();
						else{
							fechaMaxima = actividad.getFechaFin();
							
							if(ret.before(fechaMaxima))
								ret = fechaMaxima;
						}
					}
				}
			}else{
				List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(proyecto.getId(), 1);
				if(actividades != null && actividades.size() > 0){
					Date fechaMaxima = new Date();
					for(Actividad actividad : actividades){
						if(ret == null)
							ret = actividad.getFechaFin();
						else{
							fechaMaxima = actividad.getFechaFin();
							
							if(ret.before(fechaMaxima))
								ret = fechaMaxima;
						}
					}
				}else
					ret = proyecto.getFechaFin();
			}
				
		}catch(Exception e){
			CLogger.write("17", Proyecto.class, e);
		}
		
		return ret;
	}
}
