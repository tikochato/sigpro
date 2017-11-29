package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import utilities.CHibernateSession;
import utilities.CLogger;
import pojo.AsignacionRaci;
import pojo.Colaborador;

public class AsignacionRaciDAO {
	public static List<AsignacionRaci> getAsignacionesRaci(Integer objetoId, int objetoTipo, String lineaBase){
		List<AsignacionRaci> ret = new ArrayList<AsignacionRaci>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select a.* ",
											"from sipro_history.asignacion_raci a ",
											"where a.estado = 1 ",
											"and a.objeto_id = :objId ",
											"and a.objeto_tipo  = :objTipo ",
											lineaBase != null ? "and a.linea_base like '%" + lineaBase + "%'" : "and a.actual=1");
			
			Query<AsignacionRaci> criteria = session.createNativeQuery(query, AsignacionRaci.class);
			criteria.setParameter("objId", objetoId);
			criteria.setParameter("objTipo", objetoTipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", AsignacionRaciDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Colaborador> getColaboradoresPorProyecto(Integer proyectoId, String lineaBase){
		List<Colaborador> ret = new ArrayList<Colaborador>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select distinct c.*", 
					"FROM sipro_history.asignacion_raci ar",
					"inner join sipro_history.colaborador c on c.id=ar.colaboradorid",
					"where ar.objeto_tipo = 5",
					lineaBase != null ? "and ar.linea_base= like '%" + lineaBase + "%'" : "and ar.actual=1",
					"and ar.estado=1",
					"and ar.objeto_id in (", 
						"select a.id",
						"from sipro_history.actividad a",
						"where a.estado = 1",
						"and a.treePath like '"+(10000000+proyectoId)+"%'",
						lineaBase != null ? "and a.linea_base like '%" + lineaBase + "%'" : "and a.actual=1",
					")");
			
			Query<Colaborador> criteria = session.createNativeQuery(query, Colaborador.class);
			
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("2", AsignacionRaciDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static AsignacionRaci getAsignacionPorRolTarea(Integer objetoId, Integer objetoTipo , String rol, String lineaBase){
		AsignacionRaci ret = null;
		List<AsignacionRaci> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{			
			String query = String.join(" ", "select a.* from sipro_history.asignacion_raci a",
							"where a.objeto_id = ?1",
							"and a.objeto_tipo = ?2",
							"and lower(a.rol_raci) = ?3",
							"and a.estado = 1",
							lineaBase != null ? "and a.linea_base like '%" + lineaBase + "%'" : "and a.actual=1");
			
			Query<AsignacionRaci> criteria = session.createNativeQuery(query, AsignacionRaci.class);
			criteria.setParameter(1, objetoId);
			criteria.setParameter(2, objetoTipo);
			criteria.setParameter(3, rol.toLowerCase());
			
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("3", AsignacionRaciDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static Colaborador getResponsablePorRol(Integer objetoId, int objetoTipo,String rol, String lineaBase){
		Colaborador ret = null;
		List<Colaborador> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select c.* ",
									"from sipro_history.colaborador c, sipro_history.asignacion_raci a",
									"where c.id=a.colaboradorid",
									"and a.objeto_id =:objId",
									"and a.objeto_tipo =:objTipo",
									"and a.rol_raci=:rol ",
									"and a.estado=1 ",									
									lineaBase != null ? "and a.linea_base like '%" + lineaBase + "%'" : "and a.actual=1");
			
			Query<Colaborador> criteria = session.createNativeQuery(query, Colaborador.class);
			criteria.setParameter("objId", objetoId);
			criteria.setParameter("objTipo", objetoTipo);
			criteria.setParameter("rol", rol);
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("4", AsignacionRaciDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarAsignacion(AsignacionRaci asignacion){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			session.beginTransaction();
			session.saveOrUpdate(asignacion);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", AsignacionRaciDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	public static boolean eliminarTotalAsignacion(AsignacionRaci asignacion){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(asignacion);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("7", AsignacionRaciDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<AsignacionRaci> getAsignacionPorTarea(Integer objetoId, Integer objetoTipo, String lineaBase){
		List<AsignacionRaci> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select * from asignacion_raci",
							"where objeto_id = :objId",
							"and objeto_tipo = :objTipo",
							"and estado = 1");
			Query<AsignacionRaci> criteria = session.createNativeQuery(query, AsignacionRaci.class);
			criteria.setParameter("objId", objetoId);
			criteria.setParameter("objTipo", objetoTipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", AsignacionRaciDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
