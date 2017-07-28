package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.PlanAdquisiciones;
import pojo.PlanAdquisicionesDetalle;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PlanAdquisicionesDetalleDAO {
	public static int guardarPlanAdquisicion(PlanAdquisicionesDetalle planAdquisicionDetalle){
		int ret = 0;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(planAdquisicionDetalle);
			session.getTransaction().commit();
			ret = planAdquisicionDetalle.getId();
		}catch(Throwable e){
			CLogger.write("1", PlanAdquisicionesDetalleDAO.class, e);
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
			String query = "FROM PlanAdquisicionesDetalle where objetoId=:objetoId and objetoTipo=:objetoTipo";
			Query<PlanAdquisiciones> criteria = session.createQuery(query, PlanAdquisiciones.class);
			criteria.setParameter("objetoId", ObjetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getSingleResult();
		}catch(Throwable e){
			CLogger.write("2", PlanAdquisicionesDetalleDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
