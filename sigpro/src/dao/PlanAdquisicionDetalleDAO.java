package dao;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.PlanAdquisicionDetalle;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PlanAdquisicionDetalleDAO {
	public static boolean guardarPlanAdquisicion(PlanAdquisicionDetalle planAdquisicionDetalle){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(planAdquisicionDetalle);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("1", PlanAdquisicionDetalleDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static PlanAdquisicionDetalle getPlanAdquisicionByObjeto(int objetoTipo, int ObjetoId){
		PlanAdquisicionDetalle ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = "FROM PlanAdquisicionDetalle where objetoId=:objetoId and objetoTipo=:objetoTipo";
			Query<PlanAdquisicionDetalle> criteria = session.createQuery(query, PlanAdquisicionDetalle.class);
			criteria.setParameter("objetoId", ObjetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getSingleResult();
		}
		catch(NoResultException e){
			
		}
		catch(Throwable e){
			CLogger.write("2", PlanAdquisicionDetalleDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}