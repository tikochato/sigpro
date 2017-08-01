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
	public static List<AsignacionRaci> getAsignacionesRaci(Integer idActividad){
		List<AsignacionRaci> ret = new ArrayList<AsignacionRaci>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<AsignacionRaci> criteria = session.createQuery("select a from AsignacionRaci a "
					+ "where a.estado = 1 "
					+ "and a.id.actividadid = :idActividad", AsignacionRaci.class);
			criteria.setParameter("idActividad", idActividad);
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
	
	public static AsignacionRaci getAsignacionPorRolTarea(Integer actividadId, String rol){
		AsignacionRaci ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select a from AsignacionRaci a"
											,"where a.id.actividadid = :idActividad"
											,"and a.id.rolRaci = :rol"
											,"and a.estado = 1");
			
			Query<AsignacionRaci> criteria = session.createQuery(query, AsignacionRaci.class);
			criteria.setParameter("idActividad", actividadId);
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

}
