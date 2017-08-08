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
	public static List<AsignacionRaci> getAsignacionesRaci(Integer objetoId, int objetoTipo){
		List<AsignacionRaci> ret = new ArrayList<AsignacionRaci>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select a ",
											"from AsignacionRaci a ",
											"where a.estado = 1 ",
											"and a.objetoId = :objId ",
											"and a.objetoTipo  = :objTipo " );
			
			Query<AsignacionRaci> criteria = session.createQuery(query, AsignacionRaci.class);
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
	
	public static List<Colaborador> getColaboradoresPorProyecto(Integer proyectoId){
		List<Colaborador> ret = new ArrayList<Colaborador>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select distinct c from AsignacionRaci a"
				,"inner join a.matrizRaci m"
				,"inner join a.colaborador c"
				,"where m.estado = 1"
				,"and m.proyecto.id = :proyId");
			
			Query<Colaborador> criteria = session.createQuery(query, Colaborador.class);
			criteria.setParameter("proyId", proyectoId);
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
	
	public static AsignacionRaci getAsignacionPorRolTarea(Integer objetoId, Integer objetoTipo , String rol){
		AsignacionRaci ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select a from AsignacionRaci a",
							"where a.objetoId = :objId",
							"and a.objetoTipo = :objTipo",
							"and a.rolRaci = :rol",
							"and a.estado = 1");
			
			Query<AsignacionRaci> criteria = session.createQuery(query, AsignacionRaci.class);
			criteria.setParameter("objId", objetoId);
			criteria.setParameter("objTipo", objetoTipo);
			criteria.setParameter("rol", rol);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("3", AsignacionRaciDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static Colaborador getResponsablePorRol(Integer objetoId, int objetoTipo,String rol){
		Colaborador ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select c ",
									"from AsignacionRaci a", 
									"inner join a.colaborador c",
									"where a.objetoId = :objId",
									"and a.objetoTipo = :objTipo",
									"and a.rolRaci = :rol ",
									"and a.estado = 1 " );
			
			Query<Colaborador> criteria = session.createQuery(query, Colaborador.class);
			criteria.setParameter("objId", objetoId);
			criteria.setParameter("objTipo", objetoTipo);
			criteria.setParameter("rol", rol);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("4", AsignacionRaciDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
