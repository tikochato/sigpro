package dao;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.PlanAdquisicionesDetalle;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PlanAdquisicionesDetalleDAO {
	public static boolean guardarPlanAdquisicion(PlanAdquisicionesDetalle planAdquisicionDetalle){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(planAdquisicionDetalle);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("1", PlanAdquisicionesDetalleDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static PlanAdquisicionesDetalle getPlanAdquisicionByObjeto(int objetoTipo, int ObjetoId){
		PlanAdquisicionesDetalle ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = "FROM PlanAdquisicionesDetalle where objetoId=:objetoId and objetoTipo=:objetoTipo";
			Query<PlanAdquisicionesDetalle> criteria = session.createQuery(query, PlanAdquisicionesDetalle.class);
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
