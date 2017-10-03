package dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.PlanAdquisicion;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PlanAdquisicionDAO {
	public static int guardarPlanAdquisicion(PlanAdquisicion planAdquisicion){
		int ret = 0;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(planAdquisicion);
			session.getTransaction().commit();
			ret = planAdquisicion.getId();
		}catch(Throwable e){
			CLogger.write("1", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static PlanAdquisicion getPlanAdquisicionById(int planAdquisicionId){
		PlanAdquisicion ret = null;
		List<PlanAdquisicion> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = "FROM PlanAdquisicion where id=:planAdquisicionId";
			Query<PlanAdquisicion> criteria = session.createQuery(query, PlanAdquisicion.class);
			criteria.setParameter("planAdquisicionId", planAdquisicionId);
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("2", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<PlanAdquisicion> getPlanAdquisicionByObjeto(int objetoTipo, int ObjetoId){
		List<PlanAdquisicion> retList = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = "FROM PlanAdquisicion pa where pa.objetoId=:objetoId and pa.objetoTipo=:objetoTipo and pa.estado=1";
			Query<PlanAdquisicion> criteria = session.createQuery(query, PlanAdquisicion.class);
			criteria.setParameter("objetoId", ObjetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			retList = criteria.getResultList();
			
		}catch(Throwable e){
			CLogger.write("3", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
			retList = (retList.size()>0) ? retList : null;
		}
		return retList;
	}
	
	public static boolean borrarPlan(PlanAdquisicion plan){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(plan);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("4", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static List<PlanAdquisicion> getAdquisicionesNotIn(Integer objetoId, Integer objetoTipo,List<Integer> adquisiciones){
		List<PlanAdquisicion> ret = new ArrayList<PlanAdquisicion>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT pa FROM PlanAdquisicion as pa "
					+ "WHERE pa.estado = 1 "
					+ "and pa.objetoId = :objid "
					+ "and pa.objetoTipo = :objetoTipo "
					+ "and pa.id NOT IN (:ids)";
			
			Query<PlanAdquisicion> criteria = session.createQuery(query,PlanAdquisicion.class);
			criteria.setParameter("objid", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			criteria.setParameterList("ids", adquisiciones);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
}
