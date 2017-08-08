package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.PlanAdquisiciones;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PlanAdquisicionesDAO {
	public static int guardarPlanAdquisicion(PlanAdquisiciones planAdquisicion){
		int ret = 0;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(planAdquisicion);
			session.getTransaction().commit();
			ret = planAdquisicion.getId();
		}catch(Throwable e){
			CLogger.write("1", PlanAdquisicionesDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static PlanAdquisiciones getPlanAdquisicionById(int planAdquisicionId){
		PlanAdquisiciones ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = "FROM PlanAdquisiciones where id=:planAdquisicionId";
			Query<PlanAdquisiciones> criteria = session.createQuery(query, PlanAdquisiciones.class);
			criteria.setParameter("planAdquisicionId", planAdquisicionId);
			ret = criteria.getSingleResult();
		}catch(Throwable e){
			CLogger.write("2", PlanAdquisicionesDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static PlanAdquisiciones getPlanAdquisicionByObjeto(int objetoTipo, int ObjetoId){
		PlanAdquisiciones ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = "FROM PlanAdquisiciones where objetoId=:objetoId and objetoTipo=:objetoTipo";
			Query<PlanAdquisiciones> criteria = session.createQuery(query, PlanAdquisiciones.class);
			criteria.setParameter("objetoId", ObjetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getSingleResult();
		}catch(Throwable e){
			CLogger.write("3", PlanAdquisicionesDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean borrarPlan(PlanAdquisiciones plan){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(plan);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("4", PlanAdquisicionesDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
}
